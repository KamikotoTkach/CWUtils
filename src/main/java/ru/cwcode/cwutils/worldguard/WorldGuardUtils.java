package ru.cwcode.cwutils.worldguard;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.FlagContext;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.extern.java.Log;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Log
public class WorldGuardUtils {
  public static Set<ProtectedRegion> getRegionsAt(Location location) {
    RegionManager regionManager = WorldGuard.getInstance()
                                            .getPlatform()
                                            .getRegionContainer()
                                            .get(BukkitAdapter.adapt(location.getWorld()));
    
    return regionManager == null ? Collections.emptySet()
       : regionManager
       .getApplicableRegions(BukkitAdapter.asBlockVector(location))
       .getRegions();
  }
  
  @Deprecated
  public static boolean isInRegion(Location location, String region, String world) {
    return isInRegion(location, region, location.getWorld());
  }
  
  @Deprecated
  public static boolean isInRegion(Location location, String region, World world) {
    return isInRegion(location, region);
  }
  
  public static boolean isInRegion(Location location, String region) {
    if (location == null || region == null) return false;
    
    RegionManager regionManager = WorldGuard.getInstance()
                                            .getPlatform()
                                            .getRegionContainer()
                                            .get(BukkitAdapter.adapt(location.getWorld()));
    ProtectedRegion reg;
    return regionManager != null
       && ((reg = regionManager.getRegion(region)) != null
       && reg.contains(BukkitAdapter.asBlockVector(location)));
  }
  
  public static @Nullable ProtectedRegion getRegion(String region, String world) {
    return getRegion(region, Bukkit.getWorld(world));
  }
  
  public static @Nullable ProtectedRegion getRegion(String region, World world) {
    if (region == null || world == null) return null;
    
    RegionManager regionManager = WorldGuard.getInstance()
                                            .getPlatform()
                                            .getRegionContainer()
                                            .get(BukkitAdapter.adapt(world));
    if (regionManager == null) return null;
    
    return regionManager.getRegion(region);
  }
  
  public static int setFlags(ProtectedRegion region, Map<String, String> flags) {
    AtomicInteger applied = new AtomicInteger();
    flags.forEach((flag, value) -> {
      try {
        Flag flagInstance = Flags.fuzzyMatchFlag(WorldGuard.getInstance().getFlagRegistry(), flag);
        Object flagValue = flagInstance.parseInput(FlagContext.create().setInput(value).build());
        
        region.setFlag(flagInstance, flagValue);
        applied.getAndIncrement();
      } catch (Exception e) {
        log.warning("Cannot parse flag %s with value %s".formatted(flag, value));
      }
    });
    
    return applied.get();
  }
  
  public static ProtectedCuboidRegion createRegion(Location pos1, Location pos2, String name) {
    if (pos1.getWorld() != pos2.getWorld()) throw new IllegalArgumentException("pos1.world != pos2.world");
    
    RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(pos1.getWorld()));
    regionManager.removeRegion(name);
    
    ProtectedCuboidRegion region = new ProtectedCuboidRegion(name, BukkitAdapter.asBlockVector(pos1), BukkitAdapter.asBlockVector(pos2));
    
    regionManager.addRegion(region);
    
    return region;
  }
}
