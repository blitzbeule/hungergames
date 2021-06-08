package io.github.blitzbeule.hungergames.discord;

import io.github.blitzbeule.hungergames.Hungergames;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import javax.security.auth.login.LoginException;

public class HgBot implements EventListener {

    JDA jda;
    Hungergames hg;
    String guild;

    public HgBot(Hungergames hg) {
        this.hg = hg;

        guild = hg.getGsm().getConfig().getString("discord.guild-id");
        String token = hg.getGsm().getConfig().getString("discord.bot-token");
        if (token == null) {
            throw new IllegalStateException("You must specify the token in the config");
        } else if (token.equalsIgnoreCase("replace-this-with-your-token")) {
            throw new IllegalStateException("You must specify the token in the config");
        }
        JDABuilder builder = JDABuilder.createDefault(token);
        builder.setActivity(Activity.playing("Hungergames"));
        builder.addEventListeners(this);
        try {
            this.jda = builder.build();
        } catch (LoginException e) {
            hg.getLogger().severe("Discord error occurred. No bot available");
            hg.getPluginLoader().disablePlugin(hg);
        }

    }

    public void disable() {
        jda.shutdownNow();
    }

    public boolean connect(Player player, String tag) {
        User user = jda.getUserByTag(tag);
        if (user == null) {
            return false;
        }
        PrivateChannel channel = user.openPrivateChannel().complete();
        DsConnection dsc = new DsConnection(tag, user.getId());
        hg.getDsm().getConfig().set("ds-connections." + player.getUniqueId(), dsc);
        hg.getDsm().saveConfig();
        channel.sendMessage("Your Code is: " + dsc.getCode()).complete();
        channel.close().queue();
        return true;
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof MessageReceivedEvent) {
            MessageReceivedEvent e = (MessageReceivedEvent) event;
            if (e.getAuthor().isBot()) {
                return;
            }
            if (e.getChannelType() == ChannelType.PRIVATE) {
                e.getPrivateChannel().sendMessage("Hello").complete();
            } else {
                if (e.getMessage().getContentRaw().equalsIgnoreCase("_ping")) {
                    e.getChannel().sendMessage("Hello").complete();
                }
            }
        }
    }
}
