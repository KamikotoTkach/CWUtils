package ru.cwcode.cwutils.world;

import org.bukkit.*;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class WorldUtils {
  private static final ChunkGenerator emptyGenerator = new ChunkGenerator() {
    @Override
    public @NotNull ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int x, int z, @NotNull BiomeGrid biome) {
      return createChunkData(world);
    }
  };
  
  public static World createEmptyWorld(String name) {
    if (Bukkit.getWorld(name) != null) {
      return null;
    }
    
    World world = WorldCreator.name(name)
                              .type(WorldType.FLAT)
                              .generator(emptyGenerator)
                              .createWorld();
    if (world != null) {
      world.setGameRule(GameRule.RANDOM_TICK_SPEED, 0);
      world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
      world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
      world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
      
      world.setTime(6000);
      world.setClearWeatherDuration(1000);
    }
    
    return world;
  }
  
  public static World createWorld(String name, WorldType worldType) {
    if (Bukkit.getWorld(name) != null) {
      return null;
    }
    
    return WorldCreator.name(name)
                       .type(worldType)
                       .createWorld();
  }
}
