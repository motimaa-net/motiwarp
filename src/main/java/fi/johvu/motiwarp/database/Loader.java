package fi.johvu.motiwarp.database;


import fi.johvu.motiwarp.MotiWarp;
import fi.johvu.motiwarp.Utils;
import fi.johvu.motiwarp.Warp;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Loader extends BukkitRunnable {

    private final MotiWarp plugin;

    public Loader(MotiWarp plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        try (Connection connection = this.plugin.getDatabase().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from warps");
             ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                Warp warp = new Warp(
                        rs.getDouble("price"),
                        rs.getString("name"),
                        Utils.stringToLocation(rs.getString("loc")),
                        UUID.fromString(rs.getString("owner")),
                        rs.getBoolean("ownerVisible"),
                        rs.getBoolean("enchanted"),
                        Material.valueOf(rs.getString("block")),
                        rs.getString("label"),
                        rs.getLong("created"));

                plugin.getWarps().add(warp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}