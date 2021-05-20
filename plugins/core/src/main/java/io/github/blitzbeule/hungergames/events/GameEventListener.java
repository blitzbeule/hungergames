package io.github.blitzbeule.hungergames.events;

import io.github.blitzbeule.hungergames.Hungergames;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Set;

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
            Set<Player> recipients = event.recipients();
            for (Player player : recipients) {
                if (player.getGameMode() == GameMode.SPECTATOR) {
                    Component c = event.composer().composeChat(event.getPlayer(), event.getPlayer().displayName(), event.message());
                    player.sendMessage(event.getPlayer(), c, MessageType.CHAT);
                }
            }

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
