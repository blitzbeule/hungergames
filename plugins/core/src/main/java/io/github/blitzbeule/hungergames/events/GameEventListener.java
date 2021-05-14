package io.github.blitzbeule.hungergames.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class GameEventListener implements Listener {

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        if (event.getMessage().startsWith("/")) {
            if (event.getPlayer().hasPermission("hg.admin.sendIngameCommands")) {
                return;
            }
        }

        event.setCancelled(true);

    }

}
