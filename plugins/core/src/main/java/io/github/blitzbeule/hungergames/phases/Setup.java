package io.github.blitzbeule.hungergames.phases;

import io.github.blitzbeule.hungergames.Hungergames;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
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
        hg.getServer().getOnlinePlayers().forEach((Player p) -> {
            p.setFoodLevel(20);
        });
    }


    private void gamerules() {
        hg.getServer().setDefaultGameMode(GameMode.ADVENTURE);

        World world = hg.getServer().getWorld(hg.getGsm().getConfig().getString("world", "world"));

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
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().setFoodLevel(20);
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerSpawn(PlayerSpawnLocationEvent event) {
        if (event.getPlayer().hasPlayedBefore()) {
            return;
        }
        Location loc = hg.getDsm().getConfig().getLocation("setup.spawn.lobby");
        if (loc == null) {
            hg.getLogger().severe("Setup-Config is corrupted");
            return;
        }
        event.setSpawnLocation(loc);
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        if (event.getPlayer().hasPermission("hg.admin.setup.byPassSpawnEnforcement")) {
            event.allow();
            return;
        }
        Location loc = hg.getDsm().getConfig().getLocation("setup.spawn.lobby");
        if (loc == null) {
            hg.getServer().sendMessage(Component.text("Setup spawn location must be specified! Players cannot spawn", NamedTextColor.RED));
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Component.text("The server is not configured yet. Please contact your administrator if this is an error!", NamedTextColor.GOLD));
            return;
        }
    }

}
