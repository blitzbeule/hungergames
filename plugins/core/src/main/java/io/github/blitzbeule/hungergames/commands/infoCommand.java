package io.github.blitzbeule.hungergames.commands;

import io.github.blitzbeule.hungergames.Hungergames;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class infoCommand extends CommandA {

    public infoCommand(Hungergames hg) {
        super(hg);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(hg.getLmessages().welcome());
        return true;
    }
}
