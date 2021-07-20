package com.connorlinfoot.titleapi;

import org.bukkit.entity.Player;

class BukkitTitleAPI implements InternalTitleAPI {

    BukkitTitleAPI() {
    }

    @Override
    public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }

    @Override
    public void sendTabTitle(Player player, String header, String footer) {
        player.setPlayerListHeaderFooter(header, footer);
    }
}
