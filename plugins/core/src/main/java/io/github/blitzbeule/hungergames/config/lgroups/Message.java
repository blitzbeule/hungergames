package io.github.blitzbeule.hungergames.config.lgroups;

import io.github.blitzbeule.hungergames.config.LocalizationGroup;
import io.github.blitzbeule.hungergames.config.LocalizationProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Message extends LocalizationGroup implements MessageInterface {

    public Message(LocalizationProvider provider, JavaPlugin plugin) {
        super(provider, plugin);
    }

    @Override
    public String welcome() {
        return provider.getVariable("welcome");
    }


}
