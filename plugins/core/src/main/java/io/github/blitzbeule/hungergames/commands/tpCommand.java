package io.github.blitzbeule.hungergames.commands;

import io.github.blitzbeule.hungergames.Hungergames;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class tpCommand extends CommandA {

    public tpCommand(Hungergames hg) {
        super(hg);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be performed by a player!");
            return false;
        }
        Player player = (Player) sender;
        if (args.length != 1) {
            player.sendMessage("Arguments are not good.");
            return false;
        }
        switch (args[0]) {
            case "pre_lobby":
                player.teleport(hg.getDsm().getConfig().getLocation("setup.spawn.lobby"));
                player.sendActionBar(Component.text("Lobby", NamedTextColor.LIGHT_PURPLE));
                break;

            case "pre_arena":
                player.teleport(hg.getDsm().getConfig().getLocation("setup.spawn.arena"));
                player.sendActionBar(Component.text("Arena", NamedTextColor.LIGHT_PURPLE));
                break;
        }

        return true;
    }
}
