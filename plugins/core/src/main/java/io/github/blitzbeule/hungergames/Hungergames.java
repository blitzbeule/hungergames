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
import io.github.blitzbeule.hungergames.phases.PreGame;
import io.github.blitzbeule.hungergames.phases.Setup;
import io.github.blitzbeule.hungergames.storage.Match;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Hungergames extends JavaPlugin implements Listener {

    public Setup getSetupPhase() {
        return setupPhase;
    }

    private Setup setupPhase;

    public PreGame getPreGamePhase(){
        return preGamePhase;
    }

    private PreGame preGamePhase;

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

    @Override
    public void onEnable() {
        // Plugin startup logic
        ConfigurationSerialization.registerClass(Match.class, "match");
        initBeforeState();
        declarePhases();

        this.state = new State(this);

        initPhases();
        initCommands();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        gsm.saveConfig();
        dsm.saveConfig();
    }

    void declarePhases() {
        setupPhase = new Setup(this);
        preGamePhase = new PreGame(this);
    }

    void initPhases() {
        switch (state.getPhase()) {
            case SETUP:
                setupPhase.enabledOnStartup();
                return;

            case PRE_GAME:
                preGamePhase.enabledOnStartup();
                return;
        }
    }

    void initBeforeState() {
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
        if (event.getOldGamePhase().equals(event.getNew_game_phase())) {
            return;
        }
        switch (event.getOldGamePhase()) {
            case SETUP:
                setupPhase.disable();
                break;

            case PRE_GAME:
                preGamePhase.disable();
                break;
        }
        switch (event.getNew_game_phase()) {
            case SETUP:
                setupPhase.enable();
                break;

            case PRE_GAME:
                preGamePhase.enable();
                break;
        }
    }
}
