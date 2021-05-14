package io.github.blitzbeule.hungergames.commands;

import io.github.blitzbeule.hungergames.Hungergames;
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

        switch (args[0]) {
            case "info":
                return hg.getCommand("hginfo").execute(sender, "hg info", args);

            case "help":
                return  hg.getCommand("hghelp").execute(sender, "hg help", args);

            case "test":
                sender.sendMessage("No test at the moment");
                return true;

            default:
                return false;
        }
    }
}
