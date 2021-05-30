package io.github.blitzbeule.hungergames.storage;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Match implements ConfigurationSerializable {

    Player[] players;
    int rounds;
    Result[][] results;
    int current;



    public Player[] getPlayers() {
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

    public Match(Player p1, Player p2, int rounds) {
        this.rounds = rounds;
        players = new Player[2];
        players[0] = p1;
        players[1] = p2;
        results = new Result[this.rounds][2];
        current = 0;

        initResults();
    }

    public Match(Player[] players, int rounds) {
        if (players.length != 2) {
            throw new IllegalArgumentException("Array is not of correct length.");
        }
        this.players = players;
        this.rounds = rounds;
        results = new Result[this.rounds][2];
        current = 0;

        initResults();
    }

    public Match(Player p1, Player p2) {
        rounds = 10;
        players = new Player[2];
        players[0] = p1;
        players[1] = p2;
        results = new Result[this.rounds][2];
        current = 0;

        initResults();
    }

    public Match(Player[] players) {
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

    }

    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> r = new HashMap<>();
        r.put("rounds", rounds);
        r.put("players", players);
        HashMap<String, Object> rs = new HashMap<>();
        for (int i = 0; i < rounds; i++) {
            rs.put("" + i, new String[]{results[i][0].getLabel(), results[i][1].getLabel()});
        }
        r.put("results", rs);
        r.put("current", current);
        return r;
    }

    public Match(Map<String, Object> map) {

        rounds = (int) map.get("rounds");
        players = (Player[]) map.get("players");
        Map<String, Object> rs = (Map<String, Object>) map.get("results");
        results = new Result[this.rounds][2];
        for (int i = 0; i < rounds; i++) {
            String[] st = (String[]) rs.get("" + i);
            results[i][0] = Result.getByLabel(st[0]);
            results[i][1] = Result.getByLabel(st[1]);
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
