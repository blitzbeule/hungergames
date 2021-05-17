package io.github.blitzbeule.hungergames.commands;

import io.github.blitzbeule.hungergames.Hungergames;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class setupCommand extends CommandA{
    public setupCommand(Hungergames hg) {
        super(hg);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }
}
