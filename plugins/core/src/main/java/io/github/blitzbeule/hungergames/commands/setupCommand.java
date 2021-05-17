package io.github.blitzbeule.hungergames.commands;

import io.github.blitzbeule.hungergames.Hungergames;
import io.github.blitzbeule.hungergames.State;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

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

            case "-1":
            default:
                sender.sendMessage(Component.text("You must specify a valid subcommand!"));
                return false;

        }
    }
}
