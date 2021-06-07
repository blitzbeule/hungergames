package io.github.blitzbeule.hungergames.storage;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Match implements ConfigurationSerializable {

    private OfflinePlayer[] players;
    private int rounds;
    private Result[][] results;
    private int current;



    public OfflinePlayer[] getPlayers() {
        return players;
    }

    public int getRounds() {
        return rounds;
    }

    public Result[][] getResults() {
        return results;
    }

    public int getCurrent() {
        return current;
    }

    public void addResult(Result p1, Result p2) {
        results[current][0] = p1;
        results[current][1] = p2;
        current += 1;
    }

    public double getProgress() {
        return current / rounds;
    }

    public Match(OfflinePlayer p1, OfflinePlayer p2, int rounds) {
        this.rounds = rounds;
        players = new Player[2];
        players[0] = p1;
        players[1] = p2;
        results = new Result[this.rounds][2];
        current = 0;

        initResults();
    }

    public Match(OfflinePlayer[] players, int rounds) {
        if (players.length != 2) {
            throw new IllegalArgumentException("Array is not of correct length.");
        }
        this.players = players;
        this.rounds = rounds;
        results = new Result[this.rounds][2];
        current = 0;

        initResults();
    }

    public Match(OfflinePlayer p1, OfflinePlayer p2) {
        rounds = 10;
        players = new Player[2];
        players[0] = p1;
        players[1] = p2;
        results = new Result[this.rounds][2];
        current = 0;

        initResults();
    }

    public Match(OfflinePlayer[] players) {
        if (players.length != 2) {
            throw new IllegalArgumentException("Array is not of correct length.");
        }
        this.players = players;
        rounds = 10;
        results = new Result[this.rounds][2];
        current = 0;

        initResults();
    }

    private void initResults() {
        for (int i = 0; i < results.length; i++) {
            results[i][0] = Result.NONE;
            results[i][1] = Result.NONE;
        }
    }

    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> r = new HashMap<>();
        r.put("rounds", rounds);
        HashMap<String, String> pm = new HashMap<>();
        pm.put("0", players[0].getUniqueId().toString());
        pm.put("1", players[1].getUniqueId().toString());
        r.put("players", pm);
        HashMap<String, Object> rs = new HashMap<>();
        for (int i = 0; i < rounds; i++) {
            HashMap<String, String> r1 = new HashMap<>();
            r1.put("1", results[i][0].getLabel());
            r1.put("2", results[i][1].getLabel());
            rs.put("" + i, r1);
        }
        r.put("results", rs);
        r.put("current", current);
        return r;
    }

    public Match(Map<String, Object> map) {

        rounds = (int) map.get("rounds");
        players = new OfflinePlayer[2];
        Map<String, String> pmap = (Map<String, String>) map.get("players");
        players[0] = Bukkit.getOfflinePlayer(UUID.fromString(pmap.get("0")));
        players[1] = Bukkit.getOfflinePlayer(UUID.fromString(pmap.get("1")));

        Map<String, Object> rs = (Map<String, Object>) map.get("results");
        results = new Result[this.rounds][2];
        for (int i = 0; i < rounds; i++) {
            HashMap<String, String> st = (HashMap<String, String>) rs.get("" + i);
            results[i][0] = Result.getByLabel(st.get("1"));
            results[i][1] = Result.getByLabel(st.get("2"));
        }
        current = (int) map.get("current");

    }

    public enum Result {
        WIN("win"),
        LOSS("loss"),
        EVEN("even"),
        NONE("none");

        final private String label;

        Result(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public static Result getByLabel(String label) {
            return switch (label) {
                case "win" -> WIN;
                case "loss" -> LOSS;
                case "even" -> EVEN;
                case "none" -> NONE;
                default -> {throw new IllegalArgumentException();}
            };
        }
    }
}
