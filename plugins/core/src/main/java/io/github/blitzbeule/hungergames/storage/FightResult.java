package io.github.blitzbeule.hungergames.storage;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class FightResult implements ConfigurationSerializable {

    private int matches;
    private int wins;

    public OfflinePlayer getPlayer() {
        return player;
    }

    private OfflinePlayer player;
    private double mater;


    public FightResult(OfflinePlayer player) {
        matches = 0;
        wins = 0;
        this.player = player;
        mater = -1;
    }

    public void addResult(Match.Result result) {
        if (result == Match.Result.WIN) {
            wins++;
        }
        matches++;
    }

    public void setMate(FightResult r) {
        mater = r.getSoloResult();
    }

    public double getSoloResult() {
        return wins/matches;
    }

    public double getResult() {
        if (mater == -1) {
            throw new IllegalStateException("Mate result must be added first!");
        } else {
            return (((getSoloResult() * 4) + mater) / 5);
        }
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        HashMap<String, Object> r = new HashMap<>();
        r.put("wins", wins);
        r.put("matches", matches);
        r.put("player", player);
        r.put("mate", mater);
        return r;
    }

    public FightResult(Map<String, Object> map) {
        matches = (int) map.get("matches");
        wins = (int) map.get("wins");
        player = (OfflinePlayer) map.get("player");
        mater = (double) map.get("mate");
    }


    public static Set<FightResult> fullEvaluate(Match[] matches, Set<String[]> teams) {
        return addMateResult(teams, evaluate(matches));
    }

    public static Set<FightResult> evaluate(Match[] matches) {
        HashSet<OfflinePlayer> players = new HashSet<>();
        HashMap<OfflinePlayer, FightResult> map = new HashMap<>();

        for (Match m: matches) {
            players.addAll(Arrays.asList(m.getPlayers()));
        }

        for (OfflinePlayer player: players) {
            map.put(player, new FightResult(player));
        }

        for (Match m: matches) {
            Match.Result[][] results = m.getResults();
            OfflinePlayer[] mplayers = m.getPlayers();
            for (int i = 0; i < results.length; i++) {
                map.get(mplayers[0]).addResult(results[i][0]);
                map.get(mplayers[1]).addResult(results[i][1]);
            }
        }

        return new HashSet<>(map.values());
    }

    public static Set<FightResult> addMateResult(Set<String[]> teams, Set<FightResult> frs) {
        for (String[] t: teams) {
            OfflinePlayer p1 = Bukkit.getOfflinePlayerIfCached(t[0]);
            OfflinePlayer p2 = Bukkit.getOfflinePlayerIfCached(t[1]);
            FightResult r1 = null;
            FightResult r2 = null;
            for (FightResult r: frs) {
                if (r.getPlayer() == p1) {
                    r1 = r;
                    continue;
                }
                if (r.getPlayer() == p2) {
                    r2 = r;
                    continue;
                }
                if ((r1 != null) && (r2 != null)) {
                    break;
                }
            }
            r1.setMate(r2);
            r2.setMate(r1);
        }

        return frs;
    }

    public static FightResult getByPlayer(Iterable<FightResult> frs, OfflinePlayer player) {
        for (FightResult r: frs) {
            if (r.getPlayer() == player) {
                return r;
            }
        }
        return null;
    }

}
