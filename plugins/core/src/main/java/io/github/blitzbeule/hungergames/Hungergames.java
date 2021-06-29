package io.github.blitzbeule.hungergames;

import io.github.blitzbeule.hungergames.commands.*;
import io.github.blitzbeule.hungergames.config.FileLProvider;
import io.github.blitzbeule.hungergames.config.LocalizationGroups;
import io.github.blitzbeule.hungergames.config.LocalizationLanguage;
import io.github.blitzbeule.hungergames.config.SettingsManager;
import io.github.blitzbeule.hungergames.config.lgroups.Message;
import io.github.blitzbeule.hungergames.discord.DiscordWebhook;
import io.github.blitzbeule.hungergames.discord.DsConnection;
import io.github.blitzbeule.hungergames.discord.HgBot;
import io.github.blitzbeule.hungergames.phases.DiscordChat;
import io.github.blitzbeule.hungergames.phases.pregame.PreGame;
import io.github.blitzbeule.hungergames.phases.Setup;
import io.github.blitzbeule.hungergames.storage.FightResult;
import io.github.blitzbeule.hungergames.storage.Match;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.io.IOException;

public final class Hungergames extends JavaPlugin implements Listener {

    public DiscordChat getDc() {
        return dc;
    }

    private DiscordChat dc;

    public String getDw() {
        return dw;
    }

    private String dw;

    public HgBot getDsb() {
        return dsb;
    }

    private HgBot dsb;

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
        ConfigurationSerialization.registerClass(FightResult.class, "fightresult");
        ConfigurationSerialization.registerClass(DsConnection.class, "dsconnection");
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
        dsb.disable();

        DiscordWebhook ddw = new DiscordWebhook(dw);
        ddw.addEmbed(
                new DiscordWebhook.EmbedObject()
                .setColor(Color.RED)
                .setDescription("Server shutdown")
                .setAuthor("Server", "", "")
        );
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
        this.dsb = new HgBot(this);
        this.dw = gsm.getConfig().getString("discord.webhook");
        DiscordWebhook.initWebhook(new DiscordWebhook(dw));
        this.dc = new DiscordChat(this);
    }

    void initCommands() {
        this.getCommand("hg").setExecutor(new hgCommand(this));
        this.getCommand("hginfo").setExecutor(new infoCommand(this));
        this.getCommand("hgsetup").setExecutor(new setupCommand(this));
        this.getCommand("hgtp").setExecutor(new tpCommand(this));
        this.getCommand("hgpregame").setExecutor(new pregameCommand(this));
        this.getCommand("hgconnect").setExecutor(new connectCommand(this));
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
