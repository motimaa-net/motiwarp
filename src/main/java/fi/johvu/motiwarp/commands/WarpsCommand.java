package fi.johvu.motiwarp.commands;

import fi.johvu.motiwarp.MotiWarp;
import fi.johvu.motiwarp.Warp;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class WarpsCommand implements Listener, CommandExecutor {
    //ei jumalauta tää on oksettavaa mut tehää ny näi
    private final MotiWarp plugin;

    public WarpsCommand(MotiWarp plugin) {
        this.plugin = plugin;
    }

    public String getSmallest(List<String> list) {
        String smallest = list.get(0);

        for (String string : list) {
            Warp warp = plugin.getWarp(string);
            if (warp.getCreated() < plugin.getWarp(smallest).getCreated())
                smallest = string;
        }
        return smallest;
    }

    public void openWarpMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "Warp Menu");

        ItemStack yp = new ItemStack(Material.FEATHER);
        ItemMeta ypMeta = yp.getItemMeta();
        ypMeta.setDisplayName("§c§lYlläpidon Warpit");
        yp.setItemMeta(ypMeta);

        ItemStack pl = new ItemStack(Material.FEATHER);
        ItemMeta plMeta = pl.getItemMeta();
        plMeta.setDisplayName("§a§lPelaajien Warpit");
        pl.setItemMeta(plMeta);


        inv.setItem(12, yp);
        inv.setItem(14, pl);

        player.updateInventory();
        player.openInventory(inv);

    }

    public void openAdminWarps(Player player, int page) {
        Inventory inv = Bukkit.createInventory(null, 36, "Ylläpidon Warpit");

        int slot = 0;

        ArrayList<String> sortedList = new ArrayList<String>();

        ArrayList<String> createdSorted = new ArrayList<String>();


        //JOO TÄÄ ON SPAGHETTIA

        for (Warp warp : plugin.getWarps()) {
            if (warp.isEnchanted()) {
                createdSorted.add(warp.getName());
            }
        }

        for (int i = 0; i < createdSorted.size(); ) {
            sortedList.add(getSmallest(createdSorted));
            createdSorted.remove(getSmallest(createdSorted));
        }

        if (sortedList.isEmpty()) {
            player.sendMessage(plugin.getLocale().prefix() + "§7Tyhjää täynnä");
            player.closeInventory();
            return;
        }

        if (page <= 0) {
            player.closeInventory();
            return;
        }

        int showAmount = 27;
        int pagesAmount = (int) Math.ceil((float) sortedList.size() / showAmount);

        if (page > pagesAmount) {
            player.closeInventory();
            return;
        }

        for (int i = 0; i < showAmount; ) {

            List<String> lore = new ArrayList<>();

            if (i + (showAmount * (page - 1)) >= sortedList.size()) {
                break;
            }

            ItemStack is = new ItemStack(plugin.getWarp(sortedList.get(i + (showAmount * (page - 1)))).getBlock());
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(plugin.getWarp(sortedList.get(i + (showAmount * (page - 1)))).getLabel());
            if (plugin.getWarp(sortedList.get(i + (showAmount * (page - 1)))).isEnchanted()) {
                im.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
                im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            if (plugin.getConfig().getBoolean("sortedList-fee")) {
                if (!(plugin.getWarp(sortedList.get(i + (showAmount * (page - 1)))).getPrice() == 0)) {
                    lore.add(ChatColor.GOLD + "Hinta " + ChatColor.GRAY + plugin.getWarp(sortedList.get(i + (showAmount * (page - 1)))).getPrice());
                    im.setLore(lore);
                }
            }
            if (plugin.getWarp(sortedList.get(i + (showAmount * (page - 1)))).isOwnerVisible()) {
                lore.add(ChatColor.GOLD + "Omistaja " + ChatColor.GRAY + plugin.getWarp(sortedList.get(i + (showAmount * (page - 1)))).getOwnerDisplayname());
                im.setLore(lore);
            }
            is.setItemMeta(im);
            inv.setItem(slot, is);


            i++;
            slot++;

        }

        ItemStack prev = new ItemStack(Material.FEATHER);
        ItemMeta prevMeta = prev.getItemMeta();
        prevMeta.setDisplayName("Edellinen Sivu");
        prev.setItemMeta(prevMeta);

        ItemStack next = new ItemStack(Material.FEATHER);
        ItemMeta nextMeta = next.getItemMeta();
        nextMeta.setDisplayName("Seuraava Sivu");
        next.setItemMeta(nextMeta);

        ItemStack current = new ItemStack(Material.PAPER);
        ItemMeta currentMeta = current.getItemMeta();
        currentMeta.setDisplayName("Sivu: " + page);
        current.setItemMeta(currentMeta);

        inv.setItem(27, prev);
        inv.setItem(35, next);
        inv.setItem(31, current);

        player.updateInventory();
        player.openInventory(inv);

    }


    public void openPlayerWarps(Player player, int page) {
        Inventory inv = Bukkit.createInventory(null, 54, "Pelaajien Warpit");


        int slot = 0;

        ArrayList<String> sortedList = new ArrayList<String>();

        ArrayList<String> createdSorted = new ArrayList<String>();


        //JOO TÄÄ ON SPAGHETTIA

        for (Warp warp : plugin.getWarps()) {
            if (warp.isEnchanted()) {
                continue;
            }
            createdSorted.add(warp.getName());
        }

        for (int i = 0; i < createdSorted.size(); ) {
            sortedList.add(getSmallest(createdSorted));
            createdSorted.remove(getSmallest(createdSorted));
        }

        if (sortedList.isEmpty()) {
            player.sendMessage(plugin.getLocale().prefix() + "§7Tyhjää täynnä");
            player.closeInventory();
            return;
        }

        if (page <= 0) {
            player.closeInventory();
            return;
        }

        int showAmount = 36;
        int pagesAmount = (int) Math.ceil((float) sortedList.size() / showAmount);

        if (page > pagesAmount) {
            player.closeInventory();
            return;
        }

        for (int i = 0; i < showAmount; ) {

            List<String> lore = new ArrayList<>();

            if (i + (showAmount * (page - 1)) >= sortedList.size()) {
                break;
            }

            ItemStack is = new ItemStack(plugin.getWarp(sortedList.get(i + (showAmount * (page - 1)))).getBlock());
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(plugin.getWarp(sortedList.get(i + (showAmount * (page - 1)))).getLabel());
            if (plugin.getWarp(sortedList.get(i + (showAmount * (page - 1)))).isEnchanted()) {
                im.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
                im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            if (plugin.getConfig().getBoolean("sortedList-fee")) {
                if (!(plugin.getWarp(sortedList.get(i + (showAmount * (page - 1)))).getPrice() == 0)) {
                    lore.add(ChatColor.GOLD + "Hinta " + ChatColor.GRAY + plugin.getWarp(sortedList.get(i + (showAmount * (page - 1)))).getPrice());
                    im.setLore(lore);
                }
            }
            if (plugin.getWarp(sortedList.get(i + (showAmount * (page - 1)))).isOwnerVisible()) {
                lore.add(ChatColor.GOLD + "Omistaja " + ChatColor.GRAY + plugin.getWarp(sortedList.get(i + (showAmount * (page - 1)))).getOwnerDisplayname());
                im.setLore(lore);
            }
            is.setItemMeta(im);
            inv.setItem(slot, is);


            i++;
            slot++;

        }

        ItemStack prev = new ItemStack(Material.FEATHER);
        ItemMeta prevMeta = prev.getItemMeta();
        prevMeta.setDisplayName("Edellinen Sivu");
        prev.setItemMeta(prevMeta);

        ItemStack next = new ItemStack(Material.FEATHER);
        ItemMeta nextMeta = next.getItemMeta();
        nextMeta.setDisplayName("Seuraava Sivu");
        next.setItemMeta(nextMeta);

        ItemStack current = new ItemStack(Material.PAPER);
        ItemMeta currentMeta = current.getItemMeta();
        currentMeta.setDisplayName("Sivu: " + page);
        current.setItemMeta(currentMeta);

        inv.setItem(45, prev);
        inv.setItem(53, next);
        inv.setItem(49, current);

        player.updateInventory();
        player.openInventory(inv);
    }


    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {

        if (e.getView().getTitle().equals("Pelaajien Warpit") || e.getView().getTitle().equals("Ylläpidon Warpit")) {
            e.setCancelled(true);

            final ItemStack clickedItem = e.getCurrentItem();

            if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

            final Player p = (Player) e.getWhoClicked();

            String page;
            int realPage;
            //ADMINSIVUT EI TOIMI OON LAISKA NY
            switch (e.getRawSlot()) {
                case 45:
                    page = e.getInventory().getItem(49).getItemMeta().getDisplayName();
                    realPage = Integer.parseInt(page.substring(page.length() - 1));
                    if (e.getView().getTitle().equals("Pelaajien Warpit")) {
                        openPlayerWarps(p, realPage - 1);
                    } else {
                        openAdminWarps(p, realPage - 1);
                    }
                    return;

                case 53:
                    page = e.getInventory().getItem(49).getItemMeta().getDisplayName();
                    realPage = Integer.parseInt(page.substring(page.length() - 1));
                    if (e.getView().getTitle().equals("Pelaajien Warpit")) {
                        openPlayerWarps(p, realPage + 1);
                    } else {
                        openAdminWarps(p, realPage + 1);
                    }
                    return;

            }
            for (String string : plugin.getWarpsByName()) {
                if (plugin.getWarp(string).getLabel().equals(e.getCurrentItem().getItemMeta().getDisplayName())) {
                    if (!(plugin.getWarp(string).getPrice() == 0)) {
                        if (this.plugin.getEconomy().getBalance(p) >= plugin.getWarp(string).getPrice()) {
                            this.plugin.getEconomy().withdrawPlayer(p, plugin.getWarp(string).getPrice());
                            p.teleport(plugin.getWarp(string).getLoc());
                            p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 100, 0);
                            p.sendMessage(plugin.getLocale().prefix() + "Teleportattu kohteeseen ja maksu veloiotettu");
                            return;
                        }
                        p.sendMessage(plugin.getLocale().prefix() + "Ei tarpeeksi rahaa...");
                        return;
                    }
                    p.teleport(plugin.getWarp(string).getLoc());
                    p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 100, 1);
                    p.sendMessage(plugin.getLocale().prefix() + "Teleportattu kohteeseen");
                }
            }
        }
        if (e.getView().getTitle().equals("Warp Menu")) {

            e.setCancelled(true);

            final ItemStack clickedItem = e.getCurrentItem();

            if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

            final Player p = (Player) e.getWhoClicked();


            switch (e.getRawSlot()) {
                case 12:
                    openAdminWarps(p, 1);
                    return;
                case 14:
                    openPlayerWarps(p, 1);
                    return;

            }
        }
    }

    @EventHandler
    public void onInventoryDrag(final InventoryDragEvent e) {
        if (e.getView().getTitle().equals("Warp Menu") || e.getView().getTitle().equals("Ylläpidon Warpit") || e.getView().getTitle().equals("Pelaajien Warpit")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent e) {
        if (e.getView().getTitle().equals("Warp Menu") || e.getView().getTitle().equals("Ylläpidon Warpit") || e.getView().getTitle().equals("Pelaajien Warpit")) {
            Player p = (Player) e.getPlayer();
            p.playSound(p.getLocation(), Sound.BLOCK_CHEST_CLOSE, 100, 0);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            openWarpMenu(player);
            return true;
        }
        return false;
    }


//    public void openWarps(Player player, int page) {
//
//        Inventory inv = Bukkit.createInventory(null, 54, "Warpit");
//
//
//        int slot = plugin.getConfig().getInt("gui-start-slot");
//
//        ArrayList<String> sortedList = new ArrayList<String>();
//
//        ArrayList<String> createdSorted = new ArrayList<String>();
//
//
//        //JOO TÄÄ ON SPAGHETTIA
//
//        for (Warp warp : plugin.getWarps()) {
//            if (warp.isEnchanted()) {
//                sortedList.add(warp.getName());
//                continue;
//            }
//            createdSorted.add(warp.getName());
//        }
//
//        for (int i = 0; i < createdSorted.size(); ) {
//            sortedList.add(getSmallest(createdSorted));
//            createdSorted.remove(getSmallest(createdSorted));
//        }
//
//        if (sortedList.isEmpty()) {
//            player.sendMessage(plugin.getLocale().prefix() + "§7Tyhjää täynnä");
//            player.closeInventory();
//            return;
//        }
//
//        if (page <= 0) {
//            player.closeInventory();
//            return;
//        }
//
//        int showAmount = 36;
//        int pagesAmount = (int) Math.ceil((float) sortedList.size() / showAmount);
//
//        if (page > pagesAmount) {
//            player.closeInventory();
//            return;
//        }
//
//        for (int i = 0; i < showAmount; ) {
//
//            List<String> lore = new ArrayList<>();
//
//            if (i + (showAmount * (page - 1)) >= sortedList.size()) {
//                break;
//            }
//
//            ItemStack is = new ItemStack(plugin.getWarp(sortedList.get(i + (showAmount * (page - 1)))).getBlock());
//            ItemMeta im = is.getItemMeta();
//            im.setDisplayName(plugin.getWarp(sortedList.get(i + (showAmount * (page - 1)))).getLabel());
//            if (plugin.getWarp(sortedList.get(i + (showAmount * (page - 1)))).isEnchanted()) {
//                im.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
//                im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
//            }
//
//            if (plugin.getConfig().getBoolean("sortedList-fee")) {
//                if (!(plugin.getWarp(sortedList.get(i + (showAmount * (page - 1)))).getPrice() == 0)) {
//                    lore.add(ChatColor.GOLD + "Hinta " + ChatColor.GRAY + plugin.getWarp(sortedList.get(i + (showAmount * (page - 1)))).getPrice());
//                    im.setLore(lore);
//                }
//            }
//            if (plugin.getWarp(sortedList.get(i + (showAmount * (page - 1)))).isOwnerVisible()) {
//                lore.add(ChatColor.GOLD + "Omistaja " + ChatColor.GRAY + plugin.getWarp(sortedList.get(i + (showAmount * (page - 1)))).getOwnerDisplayname());
//                im.setLore(lore);
//            }
//            is.setItemMeta(im);
//            inv.setItem(slot, is);
//
//
//            i++;
//            slot++;
//
//        }
//
//        ItemStack prev = new ItemStack(Material.SNOW_BLOCK, 1);
//        ItemMeta prevMeta = prev.getItemMeta();
//        prevMeta.setDisplayName("Edellinen Sivu");
//        prev.setItemMeta(prevMeta);
//
//        ItemStack next = new ItemStack(Material.OBSIDIAN);
//
//        ItemMeta nextMeta = next.getItemMeta();
//        nextMeta.setDisplayName("Seuraava Sivu");
//        next.setItemMeta(nextMeta);
//
//        ItemStack current = new ItemStack(Material.YELLOW_BED);
//
//        ItemMeta currentMeta = current.getItemMeta();
//        currentMeta.setDisplayName("Sivu: " + page);
//        current.setItemMeta(currentMeta);
//
//        inv.setItem(45, prev);
//        inv.setItem(53, next);
//        inv.setItem(49, current);
//
//        player.updateInventory();
//        player.openInventory(inv);
//
//    }
}
