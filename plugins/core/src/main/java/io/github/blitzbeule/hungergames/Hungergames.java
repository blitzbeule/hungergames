package io.github.blitzbeule.hungergames;

import io.github.blitzbeule.hungergames.commands.hgCommand;
import io.github.blitzbeule.hungergames.commands.infoCommand;
import io.github.blitzbeule.hungergames.config.FileLProvider;
import io.github.blitzbeule.hungergames.config.LocalizationGroups;
import io.github.blitzbeule.hungergames.config.LocalizationLanguage;
import io.github.blitzbeule.hungergames.config.SettingsManager;
import io.github.blitzbeule.hungergames.config.lgroups.Message;
import org.bukkit.plugin.java.JavaPlugin;

public final class Hungergames extends JavaPlugin {

   public Message getLmessages() {
        return lmessages;
    }

    Message lmessages;

    public SettingsManager getGsm() {
        return gsm;
    }

    SettingsManager gsm;

    public SettingsManager getDsm() {
        return dsm;
    }

    SettingsManager dsm;

    public State getState() {
        return state;
    }

    State state;

    @Override
    public void onEnable() {
        // Plugin startup logic
        initBeforeState();

        this.state = new State(this);

        initCommands();
        initListeners();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        gsm.saveConfig();
        dsm.saveConfig();
    }

    void initListeners() {



    }

    void initBeforeState() {
        this.lmessages = new Message(new FileLProvider(this, LocalizationGroups.MESSAGES, LocalizationLanguage.EN), this);
        this.gsm = new SettingsManager(this, "config.yml");
        this.dsm = new SettingsManager(this, "data.yml");
    }

    void initCommands() {
        this.getCommand("hg").setExecutor(new hgCommand(this));
        this.getCommand("hginfo").setExecutor(new infoCommand(this));
    }
}
