package fi.johvu.motiwarp;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.UUID;

public class Warp {

    private final double price;
    private final String name;
    private Location loc;
    private final UUID owner;
    private final boolean ownerVisible;
    private final boolean enchanted;
    private Material block;
    private String label;
    private final long created;

    public Warp(double price, String name, Location loc, UUID owner, boolean ownerVisible, boolean enchanted, Material block, String label) {
        this.price = price;
        this.loc = loc;
        this.name = name;
        this.label = label;
        this.owner = owner;

        this.ownerVisible = ownerVisible;
        this.enchanted = enchanted;
        this.block = block;
        this.created = System.currentTimeMillis();
    }

    public Warp(double price, String name, Location loc, UUID owner, boolean ownerVisible, boolean enchanted, Material block, String label, long created) {
        this.price = price;
        this.loc = loc;
        this.name = name;
        this.label = label;
        this.owner = owner;

        this.ownerVisible = ownerVisible;
        this.enchanted = enchanted;
        this.block = block;
        this.created = created;
    }

    public UUID getOwner() {
        return owner;
    }

    public Location getLoc() {
        return loc;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public boolean isOwnerVisible() {
        return ownerVisible;
    }

    public boolean isEnchanted() {
        return enchanted;
    }

    public Material getBlock() {
        return block;
    }

    public void setBlock(Material block) {
        this.block = block;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getOwnerDisplayname() {
        return Bukkit.getOfflinePlayer(owner).getName();
    }

    public long getCreated() {
        return created;
    }
}
