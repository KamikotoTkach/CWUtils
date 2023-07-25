package tkachgeek.tkachutils.word;

import org.bukkit.*;

public class WorldUtils {
   public static World createWorld(String name, WorldType worldType) {
      if (Bukkit.getWorld(name) != null) {
         return null;
      }

      World world = WorldCreator.name(name)
                                .type(worldType)
                                .createWorld();

      return world;
   }

   public static World createFlatWorld(String name) {
      World world = createWorld(name, WorldType.FLAT);
      if (world != null) {
         world.getPopulators().clear();

         world.setGameRule(GameRule.RANDOM_TICK_SPEED, 0);
         world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
         world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
         world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
         world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);

         world.setTime(6000);
         world.setClearWeatherDuration(1000);
      }

      return world;
   }
}
