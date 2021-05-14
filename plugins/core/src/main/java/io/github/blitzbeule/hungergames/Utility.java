package io.github.blitzbeule.hungergames;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Utility {

    public static void shutdownServer(JavaPlugin plugin) {
        Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not load state");
        Bukkit.getServer().getLogger().severe(ChatColor.RED + "Going to shutdown server.");
        Bukkit.getServer().broadcastMessage(ChatColor.RED + "Internal error occurred. The server will shut down in 10s");
        Bukkit.getScheduler().runTaskLater((Plugin) plugin, () -> Bukkit.shutdown(), 200L);
    }

}
