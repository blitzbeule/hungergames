package io.github.blitzbeule.hungergames.events;

import io.github.blitzbeule.hungergames.Hungergames;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.MessageType;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.HashSet;

public class GameEventListener implements Listener {

    Hungergames hg;

    public GameEventListener(Hungergames hg) {
        this.hg = hg;
    }

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onAsyncChat(AsyncChatEvent event) {
        event.setCancelled(true);
        if (event.getPlayer().getGameMode() == GameMode.SPECTATOR) {
            HashSet<Player> players = new HashSet<>(hg.getServer().getOnlinePlayers());
            for (Player player : hg.getServer().getOnlinePlayers()) {
                if (player.getGameMode() != GameMode.SPECTATOR) {
                    players.remove(player);
                }
            }
            Audience audience = Audience.audience(
                    Audience.audience(players),
                    hg.getServer().getConsoleSender()
            );

            audience.sendMessage(
                    event.getPlayer(),
                    event.renderer().render(event.getPlayer(), event.getPlayer().displayName(), event.message(), audience),
                    MessageType.CHAT);

        }

    }

    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.getPlayer().hasPermission("hg.admin.sendIngameCommands")) {
            return;
        }
        event.setCancelled(true);
    }
    
}
