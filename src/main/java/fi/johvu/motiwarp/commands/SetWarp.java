package fi.johvu.motiwarp.commands;

import fi.johvu.motiwarp.Locale;
import fi.johvu.motiwarp.MotiWarp;
import fi.johvu.motiwarp.Warp;
import fi.johvu.motiwarp.database.Saver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetWarp implements CommandExecutor {

    private final MotiWarp plugin;

    public SetWarp(MotiWarp plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player p) {
            if (!p.hasPermission("motiwarp.player.setwarp") && !sender.isOp()) {
                p.sendMessage(plugin.getLocale().get(Locale.NO_PERMISSION));
            } else {
                if (plugin.getConfig().getList("disabled-worlds").contains(p.getWorld().getName())) {
                    p.sendMessage(plugin.getLocale().prefix() + "Et voi käyttää tätä komentoa tässä mailmassa");
                } else if (args.length == 1) {
                    Component accept = Component.text("Klikkaa tästä vahvistaaksesi warpin luonnin nimellä " + args[0] + " hintaan " + plugin.getConfig().getInt("buy-price")).color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD)
                            .clickEvent(ClickEvent.runCommand("/setwarp " + args[0] + " confirm"))
                            .hoverEvent(HoverEvent.showText(Component.text("Vahvista!").color(NamedTextColor.GREEN)));
                    p.sendMessage(accept);
                } else if (args.length != 2 || !args[1].equalsIgnoreCase("confirm")) {
                    p.sendMessage(plugin.getLocale().prefix() + "/setwarp <nimi>");
                } else {
                    boolean nameExists = plugin.getWarpsByName().stream().anyMatch(warp -> warp.equalsIgnoreCase(args[0]));

                    if (nameExists) {
                        p.sendMessage(plugin.getLocale().prefix() + "Warppi tuolla nimellä on jo olemassa!");
                    } else {
                        if (p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR) {
                            p.sendMessage(plugin.getLocale().prefix() + "Warppia ei voi asettaa ilmaan!");
                        } else if (args[0].length() > plugin.getConfig().getInt("name-maxlenght")) {
                            p.sendMessage(plugin.getLocale().prefix() + "Nimen pitää olla maksimissaan " + plugin.getConfig().getInt("name-maxlenght") + " pitkä!");
                        } else if (this.plugin.getEconomy().getBalance(p) < plugin.getConfig().getInt("buy-price")) {
                            p.sendMessage(plugin.getLocale().prefix() + "Sinulla ei ole tarpeeksi rahaa");
                        } else {
                            //JOHVU GPT START
                            Warp newWarp;
                            if (p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().isItem()) {
                                newWarp = new Warp(0, args[0], p.getLocation(), p.getUniqueId(), true, false, p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType(), args[0]);
                            } else {
                                newWarp = new Warp(0, args[0], p.getLocation(), p.getUniqueId(), true, false, Material.COMPASS, args[0]);
                            }
                            //JOHVU GPT END
                            plugin.getWarps().add(newWarp);
                            this.plugin.getEconomy().withdrawPlayer(p, plugin.getConfig().getDouble("buy-price"));
                            p.sendMessage(plugin.getLocale().prefix() + "Warppi asetettu");
                            new Saver(plugin, newWarp).runTaskAsynchronously(plugin);
                        }
                        return true;
                    }
                }
                return true;
            }
        }
        return true;
    }
}
