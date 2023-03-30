package tkachgeek.tkachutils.location;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import tkachgeek.tkachutils.numbers.Rand;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LocationUtils {
  
  /**
   * Проверяет наличие локации между двумя точками
   */
  public static boolean isInRegion(Location tested, Location pos1, Location pos2) {
    if (!tested.getWorld().equals(pos1.getWorld()) || !tested.getWorld().equals(pos2.getWorld())) {
      return false;
    }
    
    double x1 = Math.min(pos1.getX(), pos2.getX());
    double y1 = Math.min(pos1.getY(), pos2.getY());
    double z1 = Math.min(pos1.getZ(), pos2.getZ());
    
    double x2 = Math.max(pos1.getX(), pos2.getX());
    double y2 = Math.max(pos1.getY(), pos2.getY());
    double z2 = Math.max(pos1.getZ(), pos2.getZ());
    
    double x = tested.getX();
    double y = tested.getY();
    double z = tested.getZ();
    
    return
       x > x1 && x < x2
          && y > y1 && y < y2
          && z > z1 && z < z2;
  }
  
  /**
   * Возвращает случайнул локацию между двумя точками
   */
  public static Location randomLocation(Location pos1, Location pos2) {
    int x1 = Math.min(pos1.getBlockX(), pos2.getBlockX());
    int y1 = Math.min(pos1.getBlockY(), pos2.getBlockY());
    int z1 = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
    
    int x2 = Math.max(pos1.getBlockX(), pos2.getBlockX());
    int y2 = Math.max(pos1.getBlockY(), pos2.getBlockY());
    int z2 = Math.max(pos1.getBlockZ(), pos2.getBlockZ());
    
    return new Location(
       pos1.getWorld(),
       Rand.ofInt(x2 - x1 + 1) + x1,
       Rand.ofInt(y2 - y1 + 1) + y1,
       Rand.ofInt(z2 - z1 + 1) + z1
    );
  }
  
  /**
   * Возвращает локацию последнего блока воздуха под заданной локацией. Если все блоки снизу это воздух, то возвращает самый нижний блок
   */
  public static Location getHighestLocationUnder(Location location) {
    double minHeight = location.getWorld().getMinHeight();
    
    while (location.getY() < minHeight || location.getBlock().getType().isAir()) {
      location.subtract(0, 1, 0);
    }
    return location.add(0, 1, 0);
  }
  
  /**
   * Возвращает коллекцию LivingEntity в радиусе
   *
   * @param thruBlocks указывает, нужно ли делать проверку на наличие блоков между энтити
   */
  public static Collection<LivingEntity> getEntitiesInRadius(Location location, double radius, boolean thruBlocks) {
    Stream<LivingEntity> stream = location.getNearbyLivingEntities(radius).stream();
    
    if (!thruBlocks) {
      stream = stream.filter(x -> !hasBlockBetweenLocations(location, x.getLocation()));
    }
    
    return stream.collect(Collectors.toList());
  }
  
  /**
   * Проверяет наличие твёрдых блков между двумя точками (прямая)
   */
  public static boolean hasBlockBetweenLocations(Location loc1, Location loc2) {
    Vector direction = loc2.toVector().subtract(loc1.toVector()).normalize();
    double distance = loc1.distance(loc2);
    
    RayTraceResult result = loc1.getWorld().rayTraceBlocks(loc1, direction, distance, FluidCollisionMode.NEVER, true);
    
    if (result == null) {
      return false;
    }
    
    return result.getHitBlock() != null && !result.getHitBlock().getType().isAir();
  }
  
  /**
   * Проверяет есть ли энтити в заданной локации
   *
   * @param onlyAlive учитывать только LivingEntity
   */
  public static boolean hasEntityAtLocation(Location location, boolean onlyAlive) {
    for (Entity entity : location.getChunk().getEntities()) {
      if (onlyAlive && !entity.getType().isAlive()) continue;
      if (entity.getBoundingBox().contains(location.toVector())) return true;
    }
    return false;
  }
}
