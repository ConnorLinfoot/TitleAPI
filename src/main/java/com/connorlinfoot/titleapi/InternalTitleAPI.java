package com.connorlinfoot.titleapi;

import org.bukkit.entity.Player;

interface InternalTitleAPI {

    void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut);

    default void clearTitle(Player player) {
        sendTitle(player, "", "", 0, 0, 0);
    }

    void sendTabTitle(Player player, String header, String footer);

}
