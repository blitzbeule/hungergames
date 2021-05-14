package io.github.blitzbeule.hungergames.config;

import org.bukkit.plugin.java.JavaPlugin;

import java.text.MessageFormat;

public class FileLProvider implements LocalizationProvider {

    SettingsManager sm;

    public FileLProvider(JavaPlugin plugin, LocalizationGroups group, LocalizationLanguage lang) {
        sm = new SettingsManager(plugin, lang.label + ".yml", new String[]{"localization", group.file});
    }

    @Override
    public String getVariable(String name, String... arg) {
        return MessageFormat.format(sm.getConfig().getString(name), arg);
    }

}
