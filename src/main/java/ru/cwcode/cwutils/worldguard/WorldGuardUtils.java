package ru.cwcode.cwutils.worldguard;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Set;

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
}
