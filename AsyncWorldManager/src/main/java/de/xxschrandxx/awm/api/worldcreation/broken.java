package de.xxschrandxx.awm.api.worldcreation;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;

import de.xxschrandxx.api.minecraft.Config;
import de.xxschrandxx.awm.AsyncWorldManager;
import de.xxschrandxx.awm.api.config.WorldConfigManager;
import de.xxschrandxx.awm.api.config.WorldData;
import de.xxschrandxx.awm.api.event.PreWorldCreateEvent;
import de.xxschrandxx.awm.api.event.WorldCreateEvent;

public class broken {

  /**
   * Creates a World in a broken way...
   * @param preworlddata The {@link WorldData} to use.
   */
  public static void brokenworld(WorldData preworlddata) {
    Bukkit.getServer().getScheduler().runTaskAsynchronously(AsyncWorldManager.getInstance(), new Runnable() {
      @Override
      public void run() {
        WorldCreator preworldcreator = new WorldCreator(preworlddata.getWorldName());
        PreWorldCreateEvent preworldcreateevent = new PreWorldCreateEvent(preworlddata, true);
        Bukkit.getPluginManager().callEvent(preworldcreateevent);
        if (preworldcreateevent.isCancelled()) {
          return;
        }
        WorldData worlddata = preworldcreateevent.getWorldData();
        if (Bukkit.getWorld(preworldcreator.name()) == null) {
          preworldcreator.environment(worlddata.getEnviroment());
          preworldcreator.seed(worlddata.getSeed());
          preworldcreator.generator(worlddata.getGenerator());
          preworldcreator.type(worlddata.getWorldType());
          preworldcreator.generateStructures(worlddata.getGenerateStructures());
        }
        WorldCreateEvent worldcreateevent = new WorldCreateEvent(preworldcreator, true);
        Bukkit.getPluginManager().callEvent(worldcreateevent);
        if (worldcreateevent.isCancelled()) {
          return;
        }
        File worldconfigfolder = new File(AsyncWorldManager.getInstance().getDataFolder(), "worldconfigs");
        if (!worldconfigfolder.exists())
          worldconfigfolder.mkdir();
        File worldconfigfile = new File(worldconfigfolder, worlddata.getWorldName() + ".yml");
        Config config = new Config(worldconfigfile);
        WorldConfigManager.save(config, worlddata);

        WorldCreator creator = worldcreateevent.getWorldCreator();
        
        //ToDo
        
      }
    });
  }

}
