package io.github.blitzbeule.hungergames.commands;

import io.github.blitzbeule.hungergames.Hungergames;
import io.github.blitzbeule.hungergames.State;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class hgCommand extends CommandA {


    public hgCommand(Hungergames hg) {
        super(hg);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
             args = new String[]{"help"};
        }

        String[] argss;

        if (args.length > 1) {
            argss = new String[args.length - 1];
            for (int i=0; i<argss.length; i++) {
                argss[i] = args[i+1];
            }
        } else {
            argss = new String[0];
        }

        String subcommand = args[0];
        switch (subcommand) {
            case "setup":
                return  hg.getCommand("hgsetup").execute(sender, "hg setup", argss);

            case "info":
                return hg.getCommand("hginfo").execute(sender, "hg info", argss);

            case "help":
                return  hg.getCommand("hghelp").execute(sender, "hg help", argss);

            case "test":
                //sender.sendMessage("No test at the moment");
                hg.getState().setPhase(State.GamePhase.GAME);
                return true;

            case "testr":
                hg.getState().setPhase(State.GamePhase.NONE);

            default:
                return false;
        }
    }
}
