package io.github.blitzbeule.hungergames.events;

import io.github.blitzbeule.hungergames.Hungergames;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class GameEventListener implements Listener {

    Hungergames hg;

    public GameEventListener(Hungergames hg) {
        this.hg = hg;
    }

    @EventHandler
    public void onAsyncChat(AsyncChatEvent event) {
        event.setCancelled(true);

    }

    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.getPlayer().hasPermission("hg.admin.sendIngameCommands")) {
            return;
        }
        event.setCancelled(true);
    }



}
