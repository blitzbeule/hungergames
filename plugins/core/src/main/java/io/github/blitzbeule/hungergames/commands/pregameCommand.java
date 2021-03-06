package io.github.blitzbeule.hungergames.commands;

import io.github.blitzbeule.hungergames.Hungergames;
import io.github.blitzbeule.hungergames.phases.pregame.Fight;
import io.github.blitzbeule.hungergames.storage.FightResult;
import io.github.blitzbeule.hungergames.storage.Match;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class pregameCommand extends CommandA{

    public pregameCommand(Hungergames hg) {
        super(hg);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender.hasPermission("hg.admin.pregame"))) {
            sender.sendMessage("You do not have the permission to do this");
            return false;
        }

        if (args.length == 0) {
            return false;
        }

        switch (args [0]) {
            case "fight":
                return newFights(sender);

            case "test":
                return testPregame(args, sender);

            case "results":
                return saveResults(sender);
        }

        return false;
    }

    private boolean testPregame(String[] args, CommandSender sender) {

        Player p1 = Bukkit.getPlayer(args[1]);
        Player p2 = Bukkit.getPlayer(args[2]);
        int field = Integer.parseInt(args[3]);

        Match m = new Match(p1, p2, 10);
        Fight f = new Fight(0, m, field, hg);

        f.start();
        return false;
    }

    private boolean saveResults(CommandSender sender) {
        ConfigurationSection section = hg.getDsm().getConfig().getConfigurationSection("pregame.matches");

        int count = 0;
        while (section.getSerializable("" + count, Match.class) != null) {
            count++;
        }

        boolean valid = true;
        ArrayList<Match> matches = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Match match = section.getSerializable(""+i, Match.class);
            matches.add(match);
            Match.Result[][] r = match.getResults();
            for (int j = 0; j < r.length; j++) {
                for (int k = 0; k < r[j].length; k++) {
                    if (r[j][k] == Match.Result.NONE) {
                        valid = false;
                    }
                }
            }
        }
        if (!valid) {
            sender.sendMessage("Some matches are not fighted yet or an error occurred. Please check the config manually.");
            return true;
        }
        HashSet<String[]> teams = new HashSet<>();
        hg.getServer().getScoreboardManager().getMainScoreboard().getTeams().forEach((Team t) -> {
            teams.add(t.getEntries().toArray(new String[0]));
        });

        List<FightResult> results = FightResult.fullEvaluate(matches.toArray(new Match[0]), teams).stream().toList();

        for (int i = 0; i < results.size(); i++) {
            hg.getDsm().getConfig().set("pregame.results." + i, results.get(i));
        }

        return true;
    }

    private boolean newFights(CommandSender sender) {
        int current = hg.getDsm().getConfig().getInt("pregame.current", 0);
        HashMap<String, Match> matches = hg.getDsm().getConfig().getObject("pregame.matches", HashMap.class);

        if (matches.size() <= current) {
            sender.sendMessage("No more matches available");
            return true;
        }

        Fight f1;
        Fight f2;

        if (matches.size() <= (current + 1)) {
            try {
                f1 = new Fight(current, matches.get("" + current), 1, hg);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                sender.sendMessage("Failed. The players needed for next matches are not online.");
                return false;
            }
            sender.sendMessage("Only one match was left. When it is finished all matches are done.");
            f1.start();
        } else {
            try {
                f1 = new Fight(current, matches.get("" + current), 1, hg);
                f2 = new Fight((current + 1), matches.get("" + (current + 1)), 2, hg);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                sender.sendMessage("Failed. The players needed for next matches are not online.");
                return false;
            }
            f1.start();
            f2.start();
        }

        hg.getDsm().getConfig().set("pregame.current", current + 2);
        hg.getDsm().saveConfig();
        return true;
    }
}
