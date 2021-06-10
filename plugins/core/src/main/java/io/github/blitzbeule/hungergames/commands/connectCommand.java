package io.github.blitzbeule.hungergames.commands;

import io.github.blitzbeule.hungergames.Hungergames;
import io.github.blitzbeule.hungergames.discord.DsConnection;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class connectCommand extends CommandA{

    public connectCommand(Hungergames hg) {
        super(hg);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("This command must be performed by player!");
            return false;
        }
        Player player = (Player) sender;

        if (args.length == 0 || args.length > 2) {
            player.sendMessage("arguments must be correct");
            return false;
        }

        if (args[0].equalsIgnoreCase("verify")) {
            DsConnection dsc = hg.getDsm().getConfig().getSerializable("ds-connections." + player.getUniqueId(), DsConnection.class);
            if (dsc == null) {
                player.sendMessage("You must first retrieve a verification code!");
                return false;
            }
            if (dsc.confirm(args[1])) {
                hg.getDsm().getConfig().set("ds-connections." + player.getUniqueId(), dsc);
                hg.getDsm().saveConfig();
                player.sendMessage(Component.text("Successfully connected discord account", NamedTextColor.GREEN));
                return true;
            } else {
                player.sendMessage(Component.text("Code did not match! No connection established!", NamedTextColor.RED));
                return true;
            }
        } else {
            if (hg.getDsb().connect(player, args[0])) {
                player.sendMessage("Code was sent to you. Please check your Discord dm's");
                return true;
            } else {
                player.sendMessage("Something went wrong. Please check your spelling or try to ping the discord bot.");
                return true;
            }
        }
    }
}
