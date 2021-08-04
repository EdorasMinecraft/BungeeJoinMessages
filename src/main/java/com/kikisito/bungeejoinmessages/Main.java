package com.kikisito.bungeejoinmessages;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public final class Main extends Plugin {
    Configuration config;

    @Override
    public void onEnable() {
        // Generate plugin folder and default config
        if(!this.getDataFolder().exists()){
            this.getDataFolder().mkdir();
        }
        File file = new File(this.getDataFolder(), "config.yml");
        if(!file.exists()){
            try(InputStream in = this.getResourceAsStream("config.yml")){
                Files.copy(in, file.toPath());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        // Load config file
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Register listeners
        this.getProxy().getPluginManager().registerListener(this, new PlayerListener(this));
    }

    @Override
    public void onDisable() {
        this.getProxy().getPluginManager().unregisterListeners(this);
    }
}
