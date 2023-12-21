package fi.johvu.motiwarp;

import fi.johvu.motiwarp.commands.*;
import fi.johvu.motiwarp.database.Database;
import fi.johvu.motiwarp.database.Loader;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.stream.Collectors;

public final class MotiWarp extends JavaPlugin {

    private final LocaleSystem locale = new LocaleSystem(this);
    private final Database database = new Database(this);
    private final ArrayList<Warp> warps = new ArrayList<>();
    private Economy economy;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        new Loader(this).runTaskAsynchronously(this);

        getCommand("setwarp").setExecutor(new SetWarp(this));
        getCommand("warps").setExecutor(new WarpsCommand(this));
        getCommand("movewarp").setExecutor(new MoveWarp(this));
        getCommand("renamelabel").setExecutor(new RenameLabel());
        getCommand("deletewarp").setExecutor(new DeleteWarp(this));
        getCommand("setadminwarp").setExecutor(new SetAdminWarp());
        getCommand("warp").setExecutor(new WarpCommand(this));
        getCommand("motiwarp").setExecutor(new MotiWarpCommand(this));

        getServer().getPluginManager().registerEvents(new WarpsCommand(this), this);

        if (!setupEconomy()) {
            getLogger().severe("Vault not found, disabling!");
            getServer().getPluginManager().disablePlugin(this);
        }
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Database getDatabase() {
        return database;
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return true;
    }

    public Warp getWarp(String name) {
        return getWarps().stream().filter(warp -> warp.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public ArrayList<String> getWarpsByName() {
        return getWarps().stream().map(Warp::getName).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Warp> getWarps() {
        return warps;
    }

    public Economy getEconomy() {
        return economy;
    }

    public LocaleSystem getLocale() {
        return locale;
    }
}
