package com.connorlinfoot.titleapi;

import java.util.logging.Level;

enum InternalAPIMapping {
    REFLECTION(ReflectionTitleAPI.class, MinecraftVersion.v1_12),
    BUKKIT(BukkitTitleAPI.class, MinecraftVersion.v1_17),
    ;
    private final Class<? extends InternalTitleAPI> apiClass;
    private final MinecraftVersion maxVersion;

    InternalAPIMapping(Class<? extends InternalTitleAPI> apiClass, MinecraftVersion maxVersion) {
        this.apiClass = apiClass;
        this.maxVersion = maxVersion;
    }

    static InternalTitleAPI create() {
        MinecraftVersion version = MinecraftVersion.get();
        for (InternalAPIMapping thisOne : values()) {
            if (thisOne.maxVersion.isLessThanOrEqualTo(version)) {
                try {
                    return thisOne.apiClass.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    TitleAPI.instance.getLogger().log(Level.WARNING, "Failed to create InternalTitleAPI", e);
                }
            }
        }
        return null;
    }
}
