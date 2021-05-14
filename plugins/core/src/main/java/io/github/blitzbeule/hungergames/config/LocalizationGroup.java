package io.github.blitzbeule.hungergames.config;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class LocalizationGroup {

    protected LocalizationProvider provider;
    JavaPlugin plugin;

    public LocalizationGroup(LocalizationProvider provider, JavaPlugin plugin) {
        this.provider = provider;
        this.plugin = plugin;
    }

}
