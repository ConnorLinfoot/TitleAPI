package com.connorlinfoot.titleapi;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.logging.Level;


public class TitleAPI extends JavaPlugin implements Listener {
    static TitleAPI instance;

    @Deprecated
    public static void sendTitle(Player player, int fadeIn, int stay, int fadeOut, String message) {
        sendTitle(player, fadeIn, stay, fadeOut, message, null);
    }

    @Deprecated
    public static void sendSubtitle(Player player, int fadeIn, int stay, int fadeOut, String message) {
        sendTitle(player, fadeIn, stay, fadeOut, null, message);
    }

    @Deprecated
    public static void sendFullTitle(Player player, int fadeIn, int stay, int fadeOut, String title, String subtitle) {
        sendTitle(player, fadeIn, stay, fadeOut, title, subtitle);
    }

    public static void sendTitle(Player player, int fadeIn, int stay, int fadeOut, String title, String subtitle) {
        sendTitle(player, title, subtitle, fadeIn, stay, fadeOut);
    }

    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        if (instance == null) {
            Bukkit.getLogger().log(Level.WARNING, "TitleAPI was called before being enabled! Please make sure to add TitleAPI as a dependency to your plugin.yml", new RuntimeException("Too early"));
            return;
        }

        InternalTitleAPI internalApi = instance.getInternalApi().orElseThrow(() -> new RuntimeException("No internal API, Unsupported version?"));

        TitleSendEvent titleSendEvent = new TitleSendEvent(player, title, subtitle);
        Bukkit.getPluginManager().callEvent(titleSendEvent);
        if (titleSendEvent.isCancelled()) {
            return;
        }

        internalApi.sendTitle(player, title, subtitle, fadeIn, stay, fadeOut);
    }

    public static void clearTitle(Player player) {
        if (instance == null) {
            Bukkit.getLogger().log(Level.WARNING, "TitleAPI was called before being enabled! Please make sure to add TitleAPI as a dependency to your plugin.yml", new RuntimeException("Too early"));
            return;
        }

        InternalTitleAPI internalApi = instance.getInternalApi().orElseThrow(() -> new RuntimeException("No internal API, Unsupported version?"));
        internalApi.clearTitle(player);
    }

    public static void sendTabTitle(Player player, String header, String footer) {
        if (instance == null) {
            Bukkit.getLogger().log(Level.WARNING, "TitleAPI was called before being enabled! Please make sure to add TitleAPI as a dependency to your plugin.yml", new RuntimeException("Too early"));
            return;
        }

        InternalTitleAPI internalApi = instance.getInternalApi().orElseThrow(() -> new RuntimeException("No internal API, Unsupported version?"));
        internalApi.sendTabTitle(player, header, footer);
    }

    private InternalTitleAPI internalApi;

    @Override
    public void onEnable() {
        instance = this;
        getConfig().options().copyDefaults(true);
        saveConfig();

        this.internalApi = InternalAPIMapping.create();
        Bukkit.getPluginManager().registerEvents(this, this);

        if (getConfig().getBoolean("Check for Updates")) {
            CLUpdate updater = new CLUpdate(this);
            Bukkit.getPluginManager().registerEvents(updater, this);
        }
    }

    Optional<InternalTitleAPI> getInternalApi() {
        return Optional.ofNullable(internalApi);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (getConfig().getBoolean("Title On Join")) {
            sendTitle(event.getPlayer(), 20, 50, 20, getConfig().getString("Title Message"), getConfig().getString("Subtitle Message"));
        }

        if (getConfig().getBoolean("Tab Header Enabled")) {
            sendTabTitle(event.getPlayer(), getConfig().getString("Tab Header Message"), getConfig().getString("Tab Footer Message"));
        }
    }

}
