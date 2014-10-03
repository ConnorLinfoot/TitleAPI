package com.connorlinfoot.titleapi;

import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.IChatBaseComponent;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.ProtocolInjector;


public class TitleAPI extends JavaPlugin {

    public static void sendFullTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        if (craftPlayer.getHandle().playerConnection.networkManager.getVersion() != 47)
            return; // If using 1.8, allow method to run

        title = ChatColor.translateAlternateColorCodes('&', title);
        subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);

        IChatBaseComponent title2;
        IChatBaseComponent subtitle2;
        IChatBaseComponent serializedTitle = ChatSerializer.a(TextConverter.convert(title));
        IChatBaseComponent serializedSubTitle = ChatSerializer.a(TextConverter.convert(subtitle));
        title2 = serializedTitle;
        subtitle2 = serializedSubTitle;

        craftPlayer.getHandle().playerConnection.sendPacket(new ProtocolInjector.PacketTitle(ProtocolInjector.PacketTitle.Action.TIMES, fadeIn, stay, fadeOut));
        if (title != null)
            craftPlayer.getHandle().playerConnection.sendPacket(new ProtocolInjector.PacketTitle(ProtocolInjector.PacketTitle.Action.TITLE, title2));
        if (subtitle != null)
            craftPlayer.getHandle().playerConnection.sendPacket(new ProtocolInjector.PacketTitle(ProtocolInjector.PacketTitle.Action.SUBTITLE, subtitle2));
    }

    public static void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        if (craftPlayer.getHandle().playerConnection.networkManager.getVersion() != 47)
            return; // If using 1.8, allow method to run

        title = ChatColor.translateAlternateColorCodes('&', title);

        IChatBaseComponent title2;
        title2 = ChatSerializer.a(TextConverter.convert(title));

        craftPlayer.getHandle().playerConnection.sendPacket(new ProtocolInjector.PacketTitle(ProtocolInjector.PacketTitle.Action.TIMES, fadeIn, stay, fadeOut));
        if (title != null)
            craftPlayer.getHandle().playerConnection.sendPacket(new ProtocolInjector.PacketTitle(ProtocolInjector.PacketTitle.Action.TITLE, title2));
    }

    public static void sendSubtitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String subtitle) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        if (craftPlayer.getHandle().playerConnection.networkManager.getVersion() != 47)
            return; // If using 1.8, allow method to run

        subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);

        IChatBaseComponent subtitle2;
        subtitle2 = ChatSerializer.a(TextConverter.convert(subtitle));

        craftPlayer.getHandle().playerConnection.sendPacket(new ProtocolInjector.PacketTitle(ProtocolInjector.PacketTitle.Action.TIMES, fadeIn, stay, fadeOut));
        if (subtitle != null)
            craftPlayer.getHandle().playerConnection.sendPacket(new ProtocolInjector.PacketTitle(ProtocolInjector.PacketTitle.Action.SUBTITLE, subtitle2));
    }

    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();
        Server server = getServer();
        ConsoleCommandSender console = server.getConsoleSender();

        console.sendMessage("");
        console.sendMessage(ChatColor.BLUE + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
        console.sendMessage("");
        console.sendMessage(ChatColor.AQUA + getDescription().getName());
        console.sendMessage(ChatColor.AQUA + "Version " + getDescription().getVersion());
        console.sendMessage("");
        console.sendMessage(ChatColor.BLUE + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
        console.sendMessage("");
    }

    boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    public void onDisable() {
        getLogger().info(getDescription().getName() + " has been disabled!");
    }

}
