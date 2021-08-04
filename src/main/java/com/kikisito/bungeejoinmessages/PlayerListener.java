package com.kikisito.bungeejoinmessages;

import de.myzelyam.api.vanish.BungeeVanishAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerListener implements Listener {
    private final Main plugin;

    public PlayerListener(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent event){
        boolean isInvisible = BungeeVanishAPI.isInvisible(event.getPlayer());
        String message = plugin.config.getString("join-message").replace("{player}", event.getPlayer().getName());
        for(ProxiedPlayer player : plugin.getProxy().getPlayers()){
                this.sendMessage(player, isInvisible, plugin.config.getString("permissions.silent-messages.join"), plugin.config.getString("permissions.messages.join"), message);
        }
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent event){
        boolean isInvisible = BungeeVanishAPI.isInvisible(event.getPlayer());
        String message = plugin.config.getString("leave-message").replace("{player}", event.getPlayer().getName());
        for(ProxiedPlayer player : plugin.getProxy().getPlayers()){
            this.sendMessage(player, isInvisible, plugin.config.getString("permissions.silent-messages.leave"), plugin.config.getString("permissions.messages.leave"), message);
        }
    }

    @EventHandler
    public void onServerSwitch(ServerSwitchEvent event){
        if(event.getFrom() == null) {
            return;
        }

        boolean isInvisible = BungeeVanishAPI.isInvisible(event.getPlayer());
        String message = plugin.config.getString("server-switch-message")
                .replace("{player}", event.getPlayer().getName())
                .replace("{oldserver}", event.getFrom().getName())
                .replace("{newserver}", event.getPlayer().getServer().getInfo().getName());
        for(ProxiedPlayer player : plugin.getProxy().getPlayers()){
            this.sendMessage(player, isInvisible,  plugin.config.getString("permissions.silent-messages.switch"), plugin.config.getString("permissions.messages.switch"), message);
        }
    }

    private void sendMessage(ProxiedPlayer player, boolean silent, String silentPermission, String permission, String message){
        if(silent && player.hasPermission(silentPermission)){
            player.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', plugin.config.getString("silent-tag") + message)));
        } else if (!silent && player.hasPermission(permission)){
            player.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', message)));
        }
    }
}
