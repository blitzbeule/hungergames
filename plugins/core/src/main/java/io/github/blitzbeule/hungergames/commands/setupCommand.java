package io.github.blitzbeule.hungergames.commands;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import io.github.blitzbeule.hungergames.Hungergames;
import io.github.blitzbeule.hungergames.State;
import io.github.blitzbeule.hungergames.Utility;
import io.github.blitzbeule.hungergames.storage.Match;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.File;
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

        if (!(sender.hasPermission("hg.admin.setup.*"))) {
            sender.sendMessage("You do not have the permission to do this");
            return false;
        }

        String subcommand;
        if (args.length == 0) {
            subcommand = "-1";
        } else {
            subcommand = args[0];
        }

        switch (subcommand) {

            case "start":

                if (!(sender.hasPermission("hg.admin.phaseControl"))) {
                    sender.sendMessage("You do not have the permission to do this");
                    return false;
                }

                hg.getState().setPhase(State.GamePhase.SETUP);
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    boolean result = this.onCommand(player, hg.getCommand("hgsetup"), "hgsetup", new String[]{"pregame", "spawnarena"});
                    if (!result) {
                        player.sendMessage("A mess");
                        return true;
                    }
                    player.setGameMode(GameMode.CREATIVE);
                    player.setFlying(true);
                    Location loc = player.getLocation();
                    loc.setY(240);
                    player.teleport(loc.add(30, -6, 50));
                }
                return true;

            case "finish":

                if (!(sender.hasPermission("hg.admin.phaseControl"))) {
                    sender.sendMessage("You do not have the permission to do this");
                    return false;
                }

                //TODO check somehow if setup is actually finished
                hg.getState().setPhase(State.GamePhase.PRE_GAME);
                return true;

            case "spawn":
                if (args.length != 2) {
                    sender.sendMessage("Please provide correct arguments");
                    return false;
                }
                if (!(sender instanceof Player)) {
                    sender.sendMessage("This command must be performed by player");
                    return false;
                }
                Player player = (Player) sender;
                String spawn = switch (args[1]) {
                    case "lobby" -> "lobby";
                    case "arena" -> "arena";
                    default -> null;
                };
                if (spawn == null) {
                    player.sendMessage("Invalid argument");
                    return false;
                }
                hg.getDsm().getConfig().set("setup.spawn." + spawn, player.getLocation());
                hg.getDsm().saveConfig();
                player.sendMessage("Spawn is marked");
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

        Player player;

        switch (args[1]) {
            case "arenaspawn":
                if (args.length != 4) {
                    sender.sendMessage("Please provide valid syntax");
                    return false;
                }
                if (!(sender instanceof Player)) {
                    sender.sendMessage("Only Players can perform this command");
                    return false;
                }
                player = (Player) sender;

                int field = switch (args[2]) {
                    case "1" -> 1;
                    case "2" -> 2;
                    default -> -1;
                };
                int pos = switch (args[3]) {
                    case "1" -> 1;
                    case "2" -> 2;
                    default -> -1;
                };
                if (pos == -1 || field == -1) {
                    player.sendMessage("Please provide valid arguments");
                }

                Location loc = player.getLocation().toCenterLocation();
                loc.setPitch(0);
                int[] yaws = {180, 270, 0, 90};
                loc.setYaw(yaws[(Math.round((Location.normalizeYaw(loc.getYaw()) + 180) / 90f) % 4)]);
                loc = loc.add(0, 1, 0);

                hg.getDsm().getConfig().set("pregame.f-arena.spawns.field" + field + ".spawn" + pos, loc);
                hg.getDsm().saveConfig();

                return true;

            case "spawnarena":
                if (!(sender instanceof Player)) {
                    sender.sendMessage("This command must be performed by player");
                    return false;
                }
                player = (Player) sender;

                Clipboard clipboard;
                File file = new File(hg.getDataFolder(), File.separator + "schematics" + File.separator + "pre_game_arena.schem.gz");
                ClipboardFormat format = ClipboardFormats.findByAlias("sponge");
                try (ClipboardReader reader = format.getReader(hg.getResource("schematics/pre_game_arena.schem"))) {
                    clipboard = reader.read();
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
                try (EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(player.getWorld()))) {
                    Operation operation = new ClipboardHolder(clipboard)
                            .createPaste(editSession)
                            .to(BlockVector3.at(
                                    player.getLocation().getX(),
                                    240,
                                    player.getLocation().getZ()
                            ))
                            // configure here
                            .build();
                    Operations.complete(operation);
                } catch (WorldEditException e) {
                    e.printStackTrace();
                }
                return true;

            case "makematches":
                HashMap<String, String[]> teams = new HashMap<>();
                for (Team t: scoreboard.getTeams()) {
                    teams.put(t.getName(), t.getEntries().toArray(new String[t.getEntries().size()]));
                }
                return matchmaking(teams, sender);

            case "makematchest":
                HashMap<String, String[]> teamst = new HashMap<>();
                teamst.put("team1", new String[]{"p1", "p2"});
                teamst.put("team2", new String[]{"p3", "p4"});
                teamst.put("team3", new String[]{"p5", "p6"});
                teamst.put("team4", new String[]{"p7", "p8"});
                teamst.put("team5", new String[]{"p9", "p10"});
                return matchmaking(teamst, sender);
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

    boolean matchmaking(HashMap<String, String[]> teams, CommandSender sender) {
        //TODO: Validate teams HashMap

        ArrayList<String> steams = new ArrayList<>(teams.keySet());
        Collections.shuffle(steams);
        ArrayList<String[]> matches = new ArrayList<>();
        Random rgen = new Random();

        //Randomize player order
        for (int i = 0; i < steams.size(); i++) {
            String[] members = teams.get(steams.get(i)).clone();
            if (rgen.nextBoolean()) {
                String temp = members[0];
                members[0] = members[1];
                members[1] = temp;
            }
            teams.put(steams.get(i), members);
        }

        //Make matches
        boolean odd = false;
        if (teams.size() % 2 ==1) {
            steams.add("placeholder1234567890");
            odd = true;
        }
        int rounds = steams.size() - 1;
        ArrayList<int[]> indeces = new ArrayList<>();
        for (int i = 0; i < (steams.size()/2); i++) {
            indeces.add(new int[]{i, rounds-i});
        }
        for (int i = 0; i < rounds; i++) {
            for (int[] index: indeces) {
                matches.add(new String[]{steams.get(index[0]), steams.get(index[1])});
            }

            String temp = steams.get(steams.size()-1);
            for (int j = 0; j < (steams.size()-1); j++) {
                int k = (j % (steams.size() - 1)) + 1;
                String temps = steams.get(k);
                steams.set(k, temp);
                temp = temps;
            }
        }
        if (odd) {
            LinkedList<Integer> placeholderIndeces = new LinkedList<>();
            for (int i = 0; i < matches.size(); i++) {
                for (String team: matches.get(i)) {
                    if (team.equalsIgnoreCase("placeholder1234567890")) {
                        placeholderIndeces.addFirst(i);
                        break;
                    }
                }
            }
            for (int index: placeholderIndeces) {
                matches.remove(index);
            }
        }

        //Substitute teams with players
        HashMap<String, Integer> cursors = new HashMap<>();
        for (String steam: steams) {
            cursors.put(steam, 0);
        }

        for (int i = 0; i < matches.size(); i++) {
            String[] pmatch = matches.get(i);
            for (int j = 0; j < 2; j++) {
                String temp = pmatch[j];
                pmatch[j] = teams.get(temp)[(cursors.get(temp)%2)];
                cursors.put(temp, (cursors.get(temp)+ 1));
            }

        }

        Match[] r = new Match[matches.size()];
        int frounds = hg.getGsm().getConfig().getInt("stickfight-rounds", 15);
        for (int i = 0; i < matches.size(); i++) {
            OfflinePlayer p1 = hg.getServer().getOfflinePlayerIfCached(matches.get(i)[0]);
            OfflinePlayer p2 = hg.getServer().getOfflinePlayerIfCached(matches.get(i)[1]);
            r[i] = new Match(p1, p2, frounds);
        }

        HashMap<String, Match> result = new HashMap<>();

        for (int i = 0; i < r.length; i++) {
            result.put("" + i, r[i]);
        }

        hg.getDsm().getConfig().set("pregame.matches", result);
        hg.getDsm().saveConfig();

        sender.sendMessage("Matchmaking done and saved.");
        return true;

    }

}
