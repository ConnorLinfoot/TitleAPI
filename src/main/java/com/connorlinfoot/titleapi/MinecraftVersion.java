package com.connorlinfoot.titleapi;

import org.bukkit.Bukkit;

enum MinecraftVersion {
    v1_8("1_8"),
    v1_9("1_9"),
    v1_10("1_10"),
    v1_11("1_11"),
    v1_12("1_12"),
    v1_13("1_13"),
    v1_14("1_14"),
    v1_15("1_15"),
    v1_16("1_16"),
    v1_17("1_17"),
    ;
    private final String key;

    MinecraftVersion(String key) {
        this.key = key;
    }

    public boolean isGreaterThanOrEqualTo(MinecraftVersion other) {
        return ordinal() >= other.ordinal();
    }

    public boolean isLessThanOrEqualTo(MinecraftVersion other) {
        return ordinal() <= other.ordinal();
    }

    private static final String VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    private static final MinecraftVersion MINECRAFT_VERSION = MinecraftVersion.build();

    private static MinecraftVersion build() {
        for (MinecraftVersion k : MinecraftVersion.values()) {
            if (MinecraftVersion.VERSION.contains(k.key)) {
                return k;
            }
        }
        return v1_17; // Just return latest for unknown
    }

    public static MinecraftVersion get() {
        return MINECRAFT_VERSION;
    }
}