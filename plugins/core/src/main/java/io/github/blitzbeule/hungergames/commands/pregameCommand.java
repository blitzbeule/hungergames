package io.github.blitzbeule.hungergames.commands;

import io.github.blitzbeule.hungergames.Hungergames;
import io.github.blitzbeule.hungergames.phases.pregame.Fight;
import io.github.blitzbeule.hungergames.storage.Match;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

public class pregameCommand extends CommandA{

    public pregameCommand(Hungergames hg) {
        super(hg);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            return false;
        }

        switch (args [0]) {
            case "fight":
                return newFights(sender);
        }

        return false;
    }

    boolean newFights(CommandSender sender) {
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
