package io.github.blitzbeule.hungergames.phases;

import io.github.blitzbeule.hungergames.Hungergames;
import io.github.blitzbeule.hungergames.discord.DiscordWebhook;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.IOException;

public class DiscordChat implements Listener {

    Hungergames hg;

    public DiscordChat(Hungergames hg) {
        this.hg = hg;
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        String m = PlainComponentSerializer.plain().serialize(event.originalMessage());
        DiscordWebhook dw = new DiscordWebhook(hg.getDw());
        dw.addEmbed(
                new DiscordWebhook.EmbedObject()
                        .setAuthor(event.getPlayer().getName(), "", "")
                        .setDescription(m)
                        .setColor(java.awt.Color.WHITE)
        );
        try {
            dw.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
