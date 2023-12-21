package fi.johvu.motiwarp.database;

import fi.johvu.motiwarp.MotiWarp;
import fi.johvu.motiwarp.Warp;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Deleter extends BukkitRunnable {

    private final MotiWarp plugin;
    private final Warp warp;

    public Deleter(MotiWarp plugin, Warp warp) {
        this.plugin = plugin;
        this.warp = warp;
    }

    @Override
    public void run() {
        try (Connection connection = plugin.getDatabase().getConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE FROM warps where name=(?)")) {
            ps.setString(1, warp.getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
