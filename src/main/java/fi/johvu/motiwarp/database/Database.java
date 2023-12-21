package fi.johvu.motiwarp.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import fi.johvu.motiwarp.MotiWarp;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {

    public boolean isMySQL;
    private HikariDataSource hikari;
    private String url;

    public Database(MotiWarp plugin) {
        if ("sqlite".equalsIgnoreCase(plugin.getConfig().getString("storage-type"))) {
            isMySQL = false;
            url = "jdbc:sqlite:" + plugin.getDataFolder() + File.separator + "motiwarp.db";

        } else if ("mysql".equalsIgnoreCase(plugin.getConfig().getString("storage-type"))) {
            isMySQL = true;
            HikariConfig hikariConfig = new HikariConfig();

            hikariConfig.setJdbcUrl("jdbc:mysql://" + plugin.getConfig().getString("host") + ":" + plugin.getConfig().getString("port") + "/" + plugin.getConfig().getString("database") + plugin.getConfig().getString("options"));

            hikariConfig.setUsername(plugin.getConfig().getString("username"));
            hikariConfig.setPassword(plugin.getConfig().getString("password"));

            hikari = new HikariDataSource(hikariConfig);
        }

        createTables();
    }

    public Connection getConnection() throws SQLException {
        if (isMySQL) {
            return hikari.getConnection();
        } else {
            return DriverManager.getConnection(url);
        }
    }

    private void createTables() {
        String query = "CREATE TABLE IF NOT EXISTS `warps` (`name` VARCHAR(100), `price` DOUBLE ,`owner` VARCHAR(100),`loc` VARCHAR(100), `ownerVisible` BOOLEAN, `enchanted` BOOLEAN, `block` VARCHAR(100), `label` VARCHAR(100), `created` LONG, PRIMARY KEY (name))";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}