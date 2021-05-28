package io.github.blitzbeule.hungergames;

import io.github.blitzbeule.hungergames.commands.hgCommand;
import io.github.blitzbeule.hungergames.commands.infoCommand;
import io.github.blitzbeule.hungergames.commands.setupCommand;
import io.github.blitzbeule.hungergames.commands.tpCommand;
import io.github.blitzbeule.hungergames.config.FileLProvider;
import io.github.blitzbeule.hungergames.config.LocalizationGroups;
import io.github.blitzbeule.hungergames.config.LocalizationLanguage;
import io.github.blitzbeule.hungergames.config.SettingsManager;
import io.github.blitzbeule.hungergames.config.lgroups.Message;
import io.github.blitzbeule.hungergames.phases.Setup;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class Hungergames extends JavaPlugin implements Listener {

    public Setup getSetupPhase() {
        return setupPhase;
    }

    private Setup setupPhase;

    public Message getLmessages() {
        return lmessages;
    }

    private Message lmessages;

    public SettingsManager getGsm() {
        return gsm;
    }

    private SettingsManager gsm;

    public SettingsManager getDsm() {
        return dsm;
    }

    private SettingsManager dsm;

    public State getState() {
        return state;
    }

    private State state;

    HashMap<String, Listener> listeners;

    @Override
    public void onEnable() {
        // Plugin startup logic
        initBeforeState();
        declarePhases();

        this.state = new State(this);

        initPhases();
        initCommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        gsm.saveConfig();
        dsm.saveConfig();
    }

    void declarePhases() {
        setupPhase = new Setup(this);
    }

    void initPhases() {
        switch (state.getPhase()) {
            case SETUP:
                setupPhase.enabledOnStartup();
                return;
        }
    }

    void initBeforeState() {
        this.listeners = new HashMap<String, Listener>();

        this.lmessages = new Message(new FileLProvider(this, LocalizationGroups.MESSAGES, LocalizationLanguage.EN), this);
        this.gsm = new SettingsManager(this, "config.yml");
        this.dsm = new SettingsManager(this, "data.yml");
    }

    void initCommands() {
        this.getCommand("hg").setExecutor(new hgCommand(this));
        this.getCommand("hginfo").setExecutor(new infoCommand(this));
        this.getCommand("hgsetup").setExecutor(new setupCommand(this));
        this.getCommand("hgtp").setExecutor(new tpCommand(this));
    }

    @EventHandler
    public void onStateChanged(StateEvent event) {
        if (event.getOldGamePhase().equals(state.getPhase())) {
            return;
        }
        switch (event.getOldGamePhase()) {
            case SETUP:
                setupPhase.disable();
                break;
        }
        switch (state.getPhase()) {
            case SETUP:
                setupPhase.enable();
                break;
        }
    }
}
