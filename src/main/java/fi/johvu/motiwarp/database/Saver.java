package fi.johvu.motiwarp.database;


import fi.johvu.motiwarp.MotiWarp;
import fi.johvu.motiwarp.Utils;
import fi.johvu.motiwarp.Warp;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Saver extends BukkitRunnable {

    private static final String INSERTQUERYMYSQL = "INSERT INTO warps (name, price, owner, loc, ownerVisible, enchanted, block, label, created) VALUES ((?), (?), (?), (?), (?), (?), (?), (?), (?)) ON DUPLICATE KEY UPDATE loc=(?), block=(?), label=(?)";
    private static final String INSERTQUERYSQLITE = "INSERT INTO warps (name, price, owner, loc, ownerVisible, enchanted, block, label, created) VALUES ((?), (?), (?), (?), (?), (?), (?), (?), (?)) ON CONFLICT(name) DO UPDATE SET loc=(?), block=(?), label=(?)";

    private final MotiWarp plugin;
    private final Warp warp;


    public Saver(MotiWarp plugin, Warp warp) {
        this.plugin = plugin;
        this.warp = warp;
    }

    @Override
    public void run() {
        try (Connection connection = plugin.getDatabase().getConnection();
             PreparedStatement preparedStatement = plugin.getDatabase().isMySQL
                     ? connection.prepareStatement(INSERTQUERYMYSQL)
                     : connection.prepareStatement(INSERTQUERYSQLITE)) {

            preparedStatement.setString(1, warp.getName());
            preparedStatement.setDouble(2, warp.getPrice());
            preparedStatement.setString(3, warp.getOwner().toString());
            preparedStatement.setString(4, Utils.locationToString(warp.getLoc()));
            preparedStatement.setBoolean(5, warp.isOwnerVisible());
            preparedStatement.setBoolean(6, warp.isEnchanted());
            preparedStatement.setString(7, warp.getBlock().toString());
            preparedStatement.setString(8, warp.getLabel());
            preparedStatement.setLong(9, warp.getCreated());
            preparedStatement.setString(10, Utils.locationToString(warp.getLoc()));
            preparedStatement.setString(11, warp.getBlock().toString());
            preparedStatement.setString(12, warp.getLabel());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}