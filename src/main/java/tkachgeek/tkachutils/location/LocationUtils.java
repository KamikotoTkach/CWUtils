package tkachgeek.tkachutils.location;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import tkachgeek.tkachutils.numbers.Rand;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LocationUtils {
  
  /**
   * Возвращает случайную локацию между двумя точками
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
   * Возвращает список блоков, ограниченный кубоидом
   */
  public static List<Block> getAllBlocksBetween(Location pos1, Location pos2) {
    if (pos1.getWorld() != pos2.getWorld()) {
      Bukkit.getLogger().warning("getAllBlocksBetween(Location pos1, Location pos2) got positions in different dimensions");
      return List.of();
    }
    
    int xMin = Math.min(pos1.getBlockX(), pos2.getBlockX());
    int yMin = Math.min(pos1.getBlockY(), pos2.getBlockY());
    int zMin = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
    
    int xMax = Math.max(pos1.getBlockX(), pos2.getBlockX());
    int yMax = Math.max(pos1.getBlockY(), pos2.getBlockY());
    int zMax = Math.max(pos1.getBlockZ(), pos2.getBlockZ());
    
    World world = pos1.getWorld();
    
    List<Block> blocks = new ArrayList<>((Math.abs(xMin) - Math.abs(xMax))
                                            * (Math.abs(yMin) - Math.abs(yMax))
                                            * (Math.abs(zMin) - Math.abs(zMax)));
    
    for (int y = yMin; y <= yMax; y++) {
      for (int z = zMin; z <= zMax; z++) {
        for (int x = xMin; x <= xMax; x++) {
          blocks.add(world.getBlockAt(x, y, z));
        }
      }
    }
    
    return blocks;
  }
  
  /**
   * Возвращает локацию последнего блока воздуха под заданной локацией. Если все блоки снизу это воздух, то возвращает самый нижний блок
   */
  public static Location getHighestLocationUnder(Location location) {
    double minHeight = location.getWorld().getMinHeight();
    
    while (location.getY() >= minHeight && location.getBlock().getType().isAir()) {
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
  public static boolean hasBlockBetweenLocations(Location pos1, Location pos2) {
    if (!pos1.getWorld().equals(pos2.getWorld())) {
      Bukkit.getLogger().warning("hasBlockBetweenLocations(Location pos1, Location pos2) got positions in different dimensions");
      return true;
    }
    
    Vector direction = pos2.toVector().subtract(pos1.toVector()).normalize();
    double distance = pos1.distance(pos2);
    
    RayTraceResult result = pos1.getWorld().rayTraceBlocks(pos1, direction, distance, FluidCollisionMode.NEVER, true);
    
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
  
  /**
   * Проверяет есть ли энтити в заданной локации
   *
   * @param entityClass учитывать только entityClass
   */
  public static <T extends Entity> Optional<T> getEntityAtLocation(Location location, Class<T> entityClass) {
    for (Entity entity : location.getChunk().getEntities()) {
      if (!entity.getClass().isAssignableFrom(entityClass)) continue;
      if (entity.getBoundingBox().contains(location.toVector())) return Optional.of((T) entity);
    }
    return Optional.empty();
  }
  
  /**
   * Получает энтити, ограниченные кубоидом
   */
  public static Collection<Entity> getEntitiesBetween(Location pos1, Location pos2) {
    if (!pos1.getWorld().equals(pos2.getWorld())) {
      Bukkit.getLogger().warning("getEntitiesBetween(Location pos1, Location pos2) got positions in different dimensions");
      return List.of();
    }
    
    BoundingBox boundingBox = new BoundingBox(pos1.getBlockX(), pos1.getBlockY(), pos1.getBlockZ(),
                                              pos2.getBlockX(), pos2.getBlockY(), pos2.getBlockZ());
    
    return pos1.getWorld().getNearbyEntities(boundingBox);
  }
  
  /**
   * Проверяет, находится ли локация в чанке
   */
  public static boolean isIn(Location locationToTest, Chunk chunk) {
    return Chunk.getChunkKey(locationToTest) == chunk.getChunkKey();
  }
  
  /**
   * Проверяет наличие локации между двумя точками (кубоид)
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
}
