package fi.johvu.motiwarp.commands;

import fi.johvu.motiwarp.Locale;
import fi.johvu.motiwarp.MotiWarp;
import fi.johvu.motiwarp.Warp;
import fi.johvu.motiwarp.database.Deleter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DeleteWarp implements TabExecutor {

    private final MotiWarp plugin;

    public DeleteWarp(MotiWarp plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player p) {
            if (!p.hasPermission("motiwarp.player.deletewarp") && !p.isOp()) {
                p.sendMessage(plugin.getLocale().get(Locale.NO_PERMISSION));
            } else {
                if (plugin.getConfig().getList("disabled-worlds").contains(p.getWorld().getName())) {
                    p.sendMessage(plugin.getLocale().prefix() + "Et voi käyttää tätä komentoa tässä mailmassa");
                } else if (args.length == 1) {
                    Optional<Warp> warpOptional = plugin.getWarps().stream().filter(e -> e.getName().equalsIgnoreCase(args[0])).findFirst();
                    if (warpOptional.isEmpty()) {
                        p.sendMessage(plugin.getLocale().prefix() + "Tuota warppia ei ole olemassa");
                        return true;
                    }

                    Warp warp = warpOptional.get();
                    if (!warp.getOwner().equals(p.getUniqueId()) && !p.hasPermission("motiwarp.admin.removewarp")) {
                        p.sendMessage(plugin.getLocale().prefix() + "Et ole warpin omistaja");
                    } else {
                        if (warp.getOwner().equals(p.getUniqueId())) {
                            this.plugin.getEconomy().depositPlayer(p, plugin.getConfig().getInt("buy-price") * 0.75);
                        }
                        new Deleter(plugin, warp).runTaskAsynchronously(plugin);
                        plugin.getWarps().remove(warp);
                        p.sendMessage(plugin.getLocale().prefix() + "Warppi poistettu");
                    }
                } else {
                    p.sendMessage(plugin.getLocale().prefix() + "/deletewarp <nimi>");
                }
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return plugin.getWarpsByName().stream().filter(e -> e.startsWith(args[0])).collect(Collectors.toList());
    }
}
