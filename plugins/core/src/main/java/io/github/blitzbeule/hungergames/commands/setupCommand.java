package io.github.blitzbeule.hungergames.commands;

import io.github.blitzbeule.hungergames.Hungergames;
import io.github.blitzbeule.hungergames.State;
import io.github.blitzbeule.hungergames.Utility;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class setupCommand extends CommandA{
    public setupCommand(Hungergames hg) {
        super(hg);
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

            case "-1":
            default:
                sender.sendMessage(Component.text("You must specify a valid subcommand!"));
                return false;

        }
    }

    boolean setupTeams(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 1) {
            sender.sendMessage("Please use a valid operation");
            //TODO add help
            return false;
        }

        switch (args[1]) {
            case "add":
                //TODO validate input

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
                    tname = new Utility.NameGenerator(5).getName().toLowerCase().strip();
                    dname = tname;
                }

                //TODO: work with tname and dname and register a new team

                break;
        }

        return false;
    }

}
