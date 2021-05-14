package io.github.blitzbeule.hungergames.config;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class SettingsManager {
    FileConfiguration fileConfig;
    File file;
    String fileName;

    public SettingsManager(JavaPlugin plugin, String ymlName) {
        file = new File(plugin.getDataFolder(), ymlName);

        if (!file.exists()) {
            plugin.saveResource(ymlName, false);
        }
        fileName = ymlName;
        fileConfig = YamlConfiguration.loadConfiguration(file);
    }

    public SettingsManager(JavaPlugin plugin, String ymlName, String[] dirs) {
        file = new File(dirs[0]);
        if (dirs.length > 1) {
            for (int i = 1; i < dirs.length; i++) {
                file = new File(file, dirs[i]);
            }
        }
        file = new File(file, ymlName);
        File f = file;

        file = new File(plugin.getDataFolder(), file.getPath());

        if (!file.exists()) {
            plugin.saveResource(f.getPath(), false);
        }
        fileName = ymlName;
        fileConfig = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig() {
        return fileConfig;
    }

    public boolean saveConfig() {
        try {
            fileConfig.save(file);
            return true;
        } catch (IOException e) {
            Bukkit.getServer().getLogger().severe(ChatColor.RED + String.format("Could not save %s!", fileName));
            return false;
        }
    }

    public void reloadConfig() {
        fileConfig = YamlConfiguration.loadConfiguration(file);
    }
}
