package codes.towel.survivalSprint;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;

public final class SurvivalSprint extends JavaPlugin {
    EventManager eventManager;
    ArrayList<BukkitTask> activeTasks = new ArrayList<>();
    ServerConfiguration serverConf;

    @Override
    public void onEnable() {
        // Plugin startup logic
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            getLogger().info("Hooked into PlaceholderAPI");
        }
        saveDefaultConfig();
        FileConfiguration config = getConfig();

        serverConf = new ServerConfiguration(
                config.getInt("initial_border"),
                config.getInt("border_shrink"),
                config.getInt("border_shrink_speed"),
                config.getInt("day_changeover_hour"),
                config.getInt("current_day")
        );

        eventManager = new EventManager();
        activeTasks.add(Bukkit.getScheduler().runTaskTimer(this, eventManager::doEvents, 0, 20));
        activeTasks.add(Bukkit.getScheduler().runTaskTimer(this, eventManager::stopEvents, 0, 20));
    }

    @Override
    public void onDisable() {
        eventManager.closeAllEvents();

        // TODO save conf

        // Plugin shutdown logic
    }
}
