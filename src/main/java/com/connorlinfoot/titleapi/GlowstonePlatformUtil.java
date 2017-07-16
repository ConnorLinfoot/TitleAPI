package com.connorlinfoot.titleapi;

import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Utility for Bukkit/Spigot plugins aiming to support Glowstone natively.
 */
public class GlowstonePlatformUtil {
    private static Boolean glowstone;

    /**
     * Checks whether or not the current server is a Glowstone server.
     *
     * @return true if the current server is a Glowstone server, false otherwise.
     */
    public static boolean isGlowstoneServer() {
        if (glowstone != null) {
            return glowstone;
        }
        glowstone = classExists("net.glowstone.GlowServer");
        return glowstone;
    }

    public static boolean classExists(String name) {
        try {
            Class.forName(name);
        } catch (ClassNotFoundException e) {
            return false;
        }
        return true;
    }

    public static boolean isGlowstonePacket(Object packet) {
        if (!isGlowstoneServer()) {
            return false;
        }
        try {
            Class<?> messageClass = Class.forName("com.flowpowered.network.Message");
            return messageClass.isInstance(packet);
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static boolean sendPacket(Player player, Object packet) throws Exception {
        if (!isGlowstonePacket(packet)) {
            return false;
        }
        Class<?> messageClass = Class.forName("com.flowpowered.network.Message");
        Class<?> playerImplClass = Class.forName("net.glowstone.entity.GlowPlayer");
        Class<?> sessionClass = Class.forName("net.glowstone.net.GlowSession");
        if (!playerImplClass.isInstance(player)) {
            return false;
        }
        Method sessionGetter = playerImplClass.getMethod("getSession");
        Object session = sessionGetter.invoke(player);
        if (!sessionClass.isInstance(session)) {
            return false;
        }
        Method sendMethod = sessionClass.getMethod("send", messageClass);
        sendMethod.invoke(session, packet);
        return true;
    }

    public static Class<?> getClass(String name) throws ClassNotFoundException {
        if (!isGlowstoneServer()) {
            return null;
        }
        return Class.forName(name);
    }

    public static Class<?> getClass(String name, boolean array) throws ClassNotFoundException {
        if (!isGlowstoneServer()) {
            return null;
        }
        if (array) {
            return Class.forName("[L" + name + ";");
        } else {
            return Class.forName(name);
        }
    }

    public static Object constructPacket(Class<?> packetClass, Constructor constructor, Object... constructorParams) throws Exception {
        if (!isGlowstoneServer()) {
            return null;
        }
        if (packetClass == null || constructor == null) {
            return null;
        }
        Class<?> messageClass = Class.forName("com.flowpowered.network.Message");
        if (!messageClass.isAssignableFrom(packetClass)) {
            return null;
        }
        return constructor.newInstance(constructorParams);
    }
}
