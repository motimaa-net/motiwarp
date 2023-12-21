package fi.johvu.motiwarp.commands;

import fi.johvu.motiwarp.Locale;
import fi.johvu.motiwarp.MotiWarp;
import fi.johvu.motiwarp.database.Loader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MotiWarpCommand implements CommandExecutor {

    private final MotiWarp plugin;

    public MotiWarpCommand(MotiWarp plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player p) {
            switch (args[0]) {
                case "reload" -> {
                    if (p.hasPermission("motiwarp.admin.reload") || p.isOp()) {
                        this.plugin.reloadConfig();
                        this.plugin.saveDefaultConfig();
                        p.sendMessage(this.plugin.getLocale().prefix() + "Reloadattu");
                        return true;
                    }
                    p.sendMessage(plugin.getLocale().get(Locale.NO_PERMISSION));
                }
                case "fullreload" -> {
                    if (p.hasPermission("motiwarp.admin.reloadall") || p.isOp()) {
                        this.plugin.getWarps().clear();
                        this.plugin.saveDefaultConfig();
                        this.plugin.reloadConfig();
                        new Loader(this.plugin).runTaskAsynchronously(this.plugin);
                        p.sendMessage(this.plugin.getLocale().prefix() + "Reloadattu kaikki");
                        return true;
                    }
                    p.sendMessage(this.plugin.getLocale().get(Locale.NO_PERMISSION));
                }
            }
        }
        return true;
    }

}
