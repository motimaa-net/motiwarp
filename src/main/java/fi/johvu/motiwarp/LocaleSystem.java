package fi.johvu.motiwarp;

import org.bukkit.ChatColor;

import java.util.Optional;

public class LocaleSystem {

    private final MotiWarp plugin;

    public LocaleSystem(MotiWarp plugin) {
        this.plugin = plugin;
    }

    public String prefix() {
        return this.get(Locale.PREFIX);
    }

    public String get(Locale key) {
        return ChatColor.translateAlternateColorCodes('&', Optional.ofNullable(plugin.getConfig().getString(key.getPath())).orElse(key.getDefaultValue()));
    }

}
