package io.github.blitzbeule.hungergames.phases;

import io.github.blitzbeule.hungergames.Hungergames;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class Setup extends Phase{
    public Setup(Hungergames hg) {
        super(hg);
    }

    @Override
    public void enabledOnStartup() {
        gamerules();
        hg.getServer().getPluginManager().registerEvents(this, hg);
    }

    @Override
    public void enable() {
        gamerules();
        hg.getServer().getPluginManager().registerEvents(this, hg);
    }


    public void gamerules() {
        hg.getServer().setDefaultGameMode(GameMode.ADVENTURE);

        World world = hg.getServer().getWorld(hg.getGsm().getConfig().getString("world"));

        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        world.setGameRule(GameRule.KEEP_INVENTORY, true);
        world.setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);
        world.setGameRule(GameRule.DO_FIRE_TICK, false);
        world.setGameRule(GameRule.DISABLE_RAIDS, true);
        world.setGameRule(GameRule.DO_INSOMNIA, false);
        world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
        world.setGameRule(GameRule.REDUCED_DEBUG_INFO, true);
        world.setPVP(false);
        world.setDifficulty(Difficulty.PEACEFUL);
        world.setSpawnFlags(false, false);
        world.setClearWeatherDuration(100);
        world.setTime(0);
    }

    @Override
    public void disable() {
        HandlerList.unregisterAll(this);

    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerSpawn(PlayerSpawnLocationEvent event) {
        Location loc = hg.getDsm().getConfig().getLocation("setup.spawn");
        if (loc == null) {
            hg.getLogger().severe("Setup-Config is corrupted");
            return;
        }
        event.setSpawnLocation(loc);
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        if (hg.getServer().getWhitelistedPlayers().contains(Bukkit.getOfflinePlayer(event.getPlayer().getUniqueId()))) {
            event.allow();
            return;
        }
        Location loc = hg.getDsm().getConfig().getLocation("setup.spawn");
        if (loc == null) {
            hg.getServer().sendMessage(Component.text("Setup spawn location must be specified! Players cannot spawn", NamedTextColor.RED));
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Component.text("The server is not configured yet. Please contact your administrator if this is an error!", NamedTextColor.GOLD));
            return;
        }
    }

}
