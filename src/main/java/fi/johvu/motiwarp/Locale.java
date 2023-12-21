package fi.johvu.motiwarp;

public enum Locale {

    PREFIX("prefix", "&f&lMoti&a&lWarp &8&l»&r &7"),
    NO_PERMISSION("permissions_missing", "&cSinulla ei ole oikeutta tuohon komentoon");

    private final String path;
    private final String defaultValue;

    Locale(final String path, final String defaultValue) {
        this.path = path;
        this.defaultValue = defaultValue;
    }

    public String getPath() {
        return "locale." + path;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
