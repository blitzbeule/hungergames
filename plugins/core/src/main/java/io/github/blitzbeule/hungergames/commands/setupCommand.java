package io.github.blitzbeule.hungergames.commands;

import io.github.blitzbeule.hungergames.Hungergames;
import io.github.blitzbeule.hungergames.State;
import io.github.blitzbeule.hungergames.Utility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class setupCommand extends CommandA {

    Scoreboard scoreboard;
    static HashSet<NamedTextColor> colors = new HashSet<>(NamedTextColor.NAMES.values());

    public setupCommand(Hungergames hg) {
        super(hg);
        this.scoreboard = hg.getServer().getScoreboardManager().getMainScoreboard();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        String subcommand;
        if (args.length == 0) {
            subcommand = "-1";
        } else {
            subcommand = args[0];
        }

        switch (subcommand) {

            case "start":
                hg.getState().setPhase(State.GamePhase.SETUP);
                return true;

            case "finish":
                //TODO check somehow if setup is actually finished
                hg.getState().setPhase(State.GamePhase.PRE_GAME);
                return true;

            case "teams":
                return setupTeams(sender, command, label, args);

            case "pregame":
                return setupPregame(sender, command, label, args);

            case "-1":
            default:
                sender.sendMessage(Component.text("You must specify a valid subcommand!"));
                return false;

        }
    }

    private boolean setupPregame(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 1) {
            sender.sendMessage("Please use a valid operation");
            //TODO add help
            return false;
        }

        switch (args[1]) {
            case "makematches":
                HashMap<String, String[]> teams = new HashMap<>();
                for (Team t: scoreboard.getTeams()) {
                    teams.put(t.getName(), t.getEntries().toArray(new String[t.getEntries().size()]));
                }
                matchmaking(teams);
                return true;
        }

        return false;
    }

    boolean setupTeams(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 1) {
            sender.sendMessage("Please use a valid operation");
            //TODO add help
            return false;
        }

        switch (args[1]) {

            case "populate":

                ArrayList<Player> players = new ArrayList<>(hg.getServer().getOnlinePlayers());
                ArrayList<Team> teams = new ArrayList<>(scoreboard.getTeams());

                for (Player p: hg.getServer().getOnlinePlayers()) {
                    if (scoreboard.getEntryTeam(p.getName()) != null) {
                        players.remove(p);
                    }
                }
                if (players.size() == 0) {
                    sender.sendMessage("No player left with no team.");
                    return true;
                }

                for (Team t: scoreboard.getTeams()) {
                    if (t.getEntries().size() > 1) {
                        teams.remove(t);
                    }
                }
                if (teams.size() == 0) {
                    sender.sendMessage("No empty enough teams left");
                    return true;
                }

                String result = "";
                for (Player p: players) {
                    Team t = teams.get(new Random().nextInt(teams.size()));

                    //TODO: Needs testing
                    if (t.getSize() == 1) {
                        String e = t.getEntries().toArray(new String[1])[0];
                        if (e.startsWith("#")) {
                            t.removeEntry(e);
                        } else {
                            teams.remove(t);
                        }
                    }

                    t.addEntry(p.getName());
                    result = result + p.getName() + " -> " + t.getName() + " \n";
                }
                sender.sendMessage("The result was: \n" + result);
                return true;

            case "add":
                addTeam(args);
                return true;

            case "init":
                if (args.length != 3) {
                    sender.sendMessage("Please provide a valid number");
                    return false;
                }
                int n = Integer.parseInt(args[2]);
                if (n == 1 || n > 16) {
                    sender.sendMessage("Please provide a valid number");
                    return false;
                }

                for (int i = 0; i<n; i++) {
                    if (!addTeam(new String[0])) {
                        sender.sendMessage("No more teams available right now.");
                        return true;
                    }
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                sender.sendMessage("We have created the teams for you");
                return true;

        }

        return false;
    }

    boolean addTeam(String[] args) {
        //TODO: Validate input

        String tname;
        String dname;
        if (args.length > 2) {
            tname = args[2].toLowerCase().strip();

            if (args.length > 3) {
                dname = "";
                for (int i = 3; i < args.length; i++) {
                    dname = dname + args[i] + " ";
                }
                dname.strip();
            } else {
                dname = tname;
            }

        } else {
            boolean name_proofed;
            do {
                name_proofed = true;
                tname = new Utility.NameGenerator(5).getName().toLowerCase().strip();
                dname = tname;

                for (Team te: scoreboard.getTeams()) {
                    if (te.getName().equalsIgnoreCase(tname)) {
                        name_proofed = false;
                        continue;
                    }
                }
            } while (!name_proofed);
        }

        if (colors.size() == 0) return false;
        Object[] cs = colors.toArray();
        int cursor = new Random().nextInt(cs.length);
        NamedTextColor color = (NamedTextColor) cs[cursor];
        colors.remove(color);

        Team t = scoreboard.registerNewTeam(tname);

        t.color(color);
        t.displayName(Component.text(dname));
        t.addEntry("#" + tname + "fake_player_fix");
        return true;
    }

    void matchmaking(HashMap<String, String[]> teams) {
        //TODO: Validate teams HashMap

        String[] steams = teams.keySet().toArray(new String[teams.size()]);
        ArrayList<String[]> matches = new ArrayList<>();

        for (int i = 0; i < steams.length; i++) {
            for (int j = 0; j < (steams.length - (i+1)); j++) {
                String[] match = new String[2];
                match[0] = steams[i];
                match[1] = steams[j+i+1];
                matches.add(match);
            }
        }

        String r = "[";
        for (String[] s: matches) {
            r = r + Arrays.toString(s) + ",";
        }
        r = r + "]";

        System.out.println(r);

    }

}
