package com.connorlinfoot.titleapi;

import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.IChatBaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.ProtocolInjector;


public class TitleAPI extends JavaPlugin {

    public static void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        if (craftPlayer.getHandle().playerConnection.networkManager.getVersion() != 47)
            return; // If using 1.8, allow method to run

        if (title == null) title = "";
        title = ChatColor.translateAlternateColorCodes('&', title);

        if (subtitle == null) subtitle = "";
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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {
        if (args.length < 6) {
            sender.sendMessage(ChatColor.RED + "Usage: /title <player> title|subtitle <fadeIn> <stay> <fadeOut> <text>");
            sender.sendMessage(ChatColor.RED + "Note: fadeIn, stay &, fadeOut require to be a number, works in ticks; 20 = 1 second");
            return false;
        }

        if (Bukkit.getPlayer(args[0]) == null) {
            sender.sendMessage(ChatColor.RED + "Player not found");
            return false;
        }

        Player player = Bukkit.getPlayer(args[0]);

        if (!args[1].equalsIgnoreCase("title") && !args[1].equalsIgnoreCase("subtitle")) {
            sender.sendMessage(ChatColor.RED + "Invalid argument: " + args[1]);
            return false;
        }

        if (!isInteger(args[2])) {
            sender.sendMessage(ChatColor.RED + "Not a number: " + args[2]);
            return false;
        }

        if (!isInteger(args[3])) {
            sender.sendMessage(ChatColor.RED + "Not a number: " + args[3]);
            return false;
        }

        if (!isInteger(args[4])) {
            sender.sendMessage(ChatColor.RED + "Not a number: " + args[4]);
            return false;
        }

        StringBuilder builder = new StringBuilder();
        for (String value : args) {
            builder.append(value).append(" ");
        }
        String message = builder.toString();
        message = message.replace(args[0] + " ", "");
        message = message.replace(args[1] + " ", "");
        message = message.replace(args[2] + " ", "");
        message = message.replace(args[3] + " ", "");
        message = message.replace(args[4] + " ", "");

        if (args[1].equalsIgnoreCase("title")) {
            sendTitle(player, Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]), message, null);
            sender.sendMessage(ChatColor.GREEN + "Title sent");
            return true;
        }

        if (args[1].equalsIgnoreCase("subtitle")) {
            sendTitle(player, Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]), null, message);
            sender.sendMessage(ChatColor.GREEN + "Title sent");
            return true;
        }

        return false;
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
