package fi.johvu.motiwarp.commands;

import fi.johvu.motiwarp.Locale;
import fi.johvu.motiwarp.MotiWarp;
import fi.johvu.motiwarp.Warp;
import fi.johvu.motiwarp.database.Saver;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MoveWarp implements CommandExecutor {

    private final MotiWarp plugin;

    public MoveWarp(MotiWarp plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("movewarp")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (plugin.getConfig().getList("disabled-worlds").contains(p.getWorld().getName())) {
                    p.sendMessage("Et voi käyttää tätä komentoa tässä mailmassa");
                    return true;
                }
                if (p.hasPermission("motiwarp.player.movewarp") || p.isOp()) {
                    if (args.length == 1) {
                        for (Warp warp : plugin.getWarps()) {
                            if (warp.getName().equalsIgnoreCase(args[0])) {
                                if (warp.getOwner().equals(p.getUniqueId()) || p.hasPermission("motiwarp.admin.movewarp")) {
                                    if (p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR) {
                                        p.sendMessage(plugin.getLocale().prefix() + "Warppia ei voi asettaa ilmaan");
                                        return true;
                                    }
                                    if (this.plugin.getEconomy().getBalance(p) >= plugin.getConfig().getInt("move-price")) {
                                        this.plugin.getEconomy().withdrawPlayer(p, plugin.getConfig().getInt("move-price"));
                                        warp.setLoc(p.getLocation());
                                        warp.setBlock(p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType());
                                        new Saver(plugin, warp).runTaskAsynchronously(plugin);
                                        p.sendMessage(plugin.getLocale().prefix() + "Warpin paikka muutettu");
                                        return true;
                                    }
                                    p.sendMessage(plugin.getLocale().prefix() + "Sinulla ei ole tarpeeksi rahaa");
                                    return true;
                                }
                                p.sendMessage(plugin.getLocale().prefix() + "Et ole warpin omistaja");
                                return true;

                            }
                        }
                        p.sendMessage(plugin.getLocale().prefix() + "Tuota warppia ei ole olemassa");
                        return true;
                    }
                    p.sendMessage(plugin.getLocale().prefix() + "/movewarp <nimi>");
                    return true;
                }
                p.sendMessage(plugin.getLocale().get(Locale.NO_PERMISSION));
            }
        }
        return true;
    }
}
