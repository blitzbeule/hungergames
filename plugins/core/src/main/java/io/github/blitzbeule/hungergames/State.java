package io.github.blitzbeule.hungergames;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.HashMap;

public class State {

    public GamePhase getPhase() {
        return phase;
    }

    public void setPhase(GamePhase phase) {
        StateEvent e = new StateEvent(true, this.phase);
        this.phase = phase;
        this.updateConf();
        Bukkit.getPluginManager().callEvent(e);
    }

    public HashMap<String, Object> getData() {
        return data;
    }

    private GamePhase phase;
    private HashMap<String, Object> data;
    private Hungergames hg;

    public State(Hungergames hg) {
        this.hg = hg;
        updateFromConf();
    }

    private void initConf() throws Exception {
        hg.getDsm().getConfig().set("state.phase", "none");
        hg.getDsm().getConfig().set("state.data", new HashMap<String, Object>() {
        });

        if (hg.getDsm().saveConfig()) {
            hg.getDsm().reloadConfig();
            return;
        }
        throw new Exception("Error occured with config");
    }

    public void updateFromConf() {
        try {

            if (hg.getDsm().getConfig().contains("state")) {
                String phase = hg.getDsm().getConfig().getConfigurationSection("state").getString("phase", "none");
                this.phase = GamePhase.labelOf(phase);

                this.data = (HashMap<String, Object>) hg.getDsm().getConfig().getConfigurationSection("state").getConfigurationSection("data").getValues(true);

            } else {

                initConf();
                updateFromConf();

            }
        } catch (Exception e) {
            Utility.shutdownServer(hg);
        }
    }

    public void updateConf() {

        try {
            hg.getDsm().getConfig().set("state.phase", phase.label);
            hg.getDsm().getConfig().set("state.data", data);
            hg.getDsm().saveConfig();
        } catch (Exception e) {
            Utility.shutdownServer(hg);
        }


    }

    public enum GamePhase {
        NONE("none"),
        PRE_GAME("pre"),
        QUALI("quali"),
        START("start"),
        GAME("game"),
        END("end");

        public final String label;

        public static GamePhase labelOf(String label) throws IllegalArgumentException {
            switch (label) {
                case "none":
                    return GamePhase.NONE;

                case "pre":
                    return GamePhase.PRE_GAME;

                case "quali":
                    return GamePhase.QUALI;

                case "start":
                    return GamePhase.START;

                case "game":
                    return GamePhase.GAME;

                case "end":
                    return GamePhase.END;
            }
            throw new IllegalArgumentException("label not defined");
        }

        GamePhase(String label) {
            this.label = label;
        }

    }

}
