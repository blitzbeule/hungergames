package io.github.blitzbeule.hungergames.phases;

import io.github.blitzbeule.hungergames.Hungergames;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.event.HandlerList;

public class PreGame extends Phase{

    public PreGame(Hungergames hg) {
        super(hg);
    }

    private void gamerules() {
        hg.getServer().setDefaultGameMode(GameMode.ADVENTURE);

        World world = hg.getServer().getWorld(hg.getGsm().getConfig().getString("world", "world"));

        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        world.setGameRule(GameRule.KEEP_INVENTORY, true);
        world.setGameRule(GameRule.SHOW_DEATH_MESSAGES, true);
        world.setGameRule(GameRule.DO_FIRE_TICK, false);
        world.setGameRule(GameRule.DISABLE_RAIDS, true);
        world.setGameRule(GameRule.DO_INSOMNIA, false);
        world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
        world.setGameRule(GameRule.REDUCED_DEBUG_INFO, true);
        world.setPVP(true);
        world.setDifficulty(Difficulty.HARD);
        world.setSpawnFlags(false, false);
        world.setClearWeatherDuration(100);
        world.setTime(0);
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

    @Override
    public void disable() {
        HandlerList.unregisterAll(this);
    }
}
