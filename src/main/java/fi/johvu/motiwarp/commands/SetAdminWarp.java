package fi.johvu.motiwarp.commands;

import fi.johvu.motiwarp.Locale;
import fi.johvu.motiwarp.MotiWarp;
import fi.johvu.motiwarp.Warp;
import fi.johvu.motiwarp.database.Saver;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetAdminWarp implements CommandExecutor {
    MotiWarp plugin = MotiWarp.getPlugin(MotiWarp.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("setadminwarp")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (p.hasPermission("motiwarp.admin.setwarp") || sender.isOp()) {
                    if (args.length == 3) {
                        double amount;
                        boolean bole = true;
                        if (!args[2].equalsIgnoreCase("true") && !args[2].equalsIgnoreCase("false")) {
                            p.sendMessage(plugin.getLocale().prefix() + "§7False tai true");
                            p.sendMessage(plugin.getLocale().prefix() + "§7/setadminwarp [nimi] [hinta] [näkyykö omistaja | true/false]");
                            return true;
                        }
                        try {
                            bole = Boolean.parseBoolean(args[2]);
                        } catch (Exception e) {
                            e.printStackTrace();
                            p.sendMessage(plugin.getLocale().prefix() + "§7Booleanin pitää olla true tai false");
                            p.sendMessage(plugin.getLocale().prefix() + "§7/setadminwarp [nimi] [hinta] [näkyykö omistaja | true/false]");
                            return true;
                        }

                        try {
                            amount = Double.parseDouble(args[1]);
                        } catch (NumberFormatException exception) {
                            p.sendMessage(plugin.getLocale().prefix() + "§7Virheellinen luku");
                            p.sendMessage(plugin.getLocale().prefix() + "§7/setadminwarp [nimi] [hinta] [näkyykö omistaja | true/false]");
                            return true;
                        }

                        boolean nameExists = false;

                        for (Warp warp : plugin.getWarps()) {
                            if (warp.getName().equalsIgnoreCase(args[0])) {
                                nameExists = true;
                                break;
                            }

                        }
                        if (!nameExists) {
                            Warp warp = new Warp(amount, args[0], p.getLocation(), p.getUniqueId(), bole, true, p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType(), args[0]);
                            plugin.getWarps().add(warp);
                            p.sendMessage(plugin.getLocale().prefix() + "§7Admin Warppi asetettu");
                            new Saver(plugin, warp).runTaskAsynchronously(plugin);
                            return true;
                        }
                        p.sendMessage(plugin.getLocale().prefix() + "WarpCommand tuolla nimellä on jo olemassa!");
                        return true;
                    }
                    p.sendMessage(plugin.getLocale().prefix() + "§7/setadminwarp [nimi] [hinta] [näkyykö omistaja | true/false]");
                    return true;

                }
                p.sendMessage(this.plugin.getLocale().get(Locale.NO_PERMISSION));
                return true;
            }

        }
        return true;
    }

}

