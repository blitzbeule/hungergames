package io.github.blitzbeule.hungergames.discord;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DsConnection implements ConfigurationSerializable {

    public int getCode() {
        return code;
    }

    public String getDiscordTag() {
        return discordTag;
    }

    public String getDiscordId() {
        return discordId;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public boolean confirm(String code) {
        if (code.equalsIgnoreCase("" + code)) {
            confirmed = true;
            return true;
        }
        return false;
    }

    public boolean confirm(int code) {
        if (code == code) {
            confirmed = true;
            return true;
        }
        return false;
    }

    int code;
    String discordTag;
    String discordId;
    boolean confirmed;

    public DsConnection(String discordTag, String discordId) {
        this.discordId = discordId;
        this.discordTag = discordTag;
        confirmed = false;
        code = (int) ((new Random()).nextFloat() * 100000);
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        HashMap<String, Object> r = new HashMap<>();

        r.put("ds-id", discordId);
        r.put("ds-tag", discordTag);
        r.put("confirmed", confirmed);
        r.put("code", code);

        return r;
    }
    public DsConnection(Map<String, Object> map) {
        this.discordTag = (String) map.get("ds-tag");
        this.discordId = (String) map.get("ds-id");
        this.code = (int) map.get("code");
        this.confirmed = (boolean) map.get("confirmed");
    }
}
