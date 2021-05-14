package io.github.blitzbeule.hungergames.commands;

import io.github.blitzbeule.hungergames.Hungergames;
import org.bukkit.command.CommandExecutor;

public abstract class CommandA implements CommandExecutor {

    protected Hungergames hg;

    public CommandA(Hungergames hg) {
        this.hg = hg;
    }

}
