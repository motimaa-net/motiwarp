package fi.johvu.motiwarp.commands;

import fi.johvu.motiwarp.Locale;
import fi.johvu.motiwarp.MotiWarp;
import fi.johvu.motiwarp.Warp;
import fi.johvu.motiwarp.database.Saver;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RenameLabel implements CommandExecutor {
    MotiWarp plugin = MotiWarp.getPlugin(MotiWarp.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("renamelabel")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (p.hasPermission("motiwarp.admin") || p.isOp()) {
                    if (args.length == 2) {

                        boolean labelExists = false;

                        for (Warp warp : plugin.getWarps()) {
                            if (warp.getLabel().equalsIgnoreCase(args[1])) {
                                labelExists = true;
                                break;
                            }

                        }

                        if (!labelExists) {
                            Warp warp = plugin.getWarp(args[0]);
                            if (warp != null) {
                                if (warp.getName().equalsIgnoreCase(args[0])) {
                                    warp.setLabel(args[1]);
                                    new Saver(plugin, warp).runTaskAsynchronously(plugin);
                                    p.sendMessage(plugin.getLocale().prefix() + "Warpin label muutettu");
                                    return true;
                                }
                            }
                            p.sendMessage(plugin.getLocale().prefix() + "Tuollaista warppia ei ole...");
                            return true;
                        }
                        p.sendMessage(plugin.getLocale().prefix() + "Label on jo käytössä.");
                        return true;
                    }
                    p.sendMessage(plugin.getLocale().prefix() + "/renamelabel <warpinnimi> <uusilabel>");
                    return true;
                }
                p.sendMessage(plugin.getLocale().get(Locale.NO_PERMISSION));
            }
        }
        return true;
    }
}

