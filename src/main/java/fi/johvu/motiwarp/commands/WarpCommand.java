package fi.johvu.motiwarp.commands;

import fi.johvu.motiwarp.MotiWarp;
import fi.johvu.motiwarp.Warp;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class WarpCommand implements TabExecutor {

    private final MotiWarp plugin;

    public WarpCommand(MotiWarp plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player p) {
            if (args.length == 1) {
                if (plugin.getConfig().getList("disabled-worlds").contains(p.getWorld().getName())) {
                    p.sendMessage(plugin.getLocale().prefix() + "Et voi käyttää tätä komentoa tässä mailmassa");
                    return true;
                }
                for (Warp warp : plugin.getWarps()) {
                    if (warp.getName().equalsIgnoreCase(args[0])) {
                        p.teleport(warp.getLoc());
                        p.sendMessage(plugin.getLocale().prefix() + "Teleportattu kohteeseen");
                        p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 100, 1);
                        return true;
                    }
                }
            }
            p.performCommand("warps");
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
      return plugin.getWarpsByName().stream().filter(e -> e.startsWith(args[0])).collect(Collectors.toList());
    }
}
