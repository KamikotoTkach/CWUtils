package ru.cwcode.cwutils.location;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Range;
import ru.cwcode.cwutils.numbers.Rand;
import ru.cwcode.cwutils.particles.ParticlesUtils;

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
    
    List<Block> blocks = new ArrayList<>();
    
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
       x >= x1 && x <= x2
          && y >= y1 && y <= y2
          && z >= z1 && z <= z2;
  }
  
  /**
   * Проверяет пересечение двух регионов по четырём точками (параллелепипеды)
   */
  public static boolean isIntersecting(Location region1Pos1, Location region1Pos2, Location region2Pos1, Location region2Pos2) {
    if (!region1Pos1.getWorld().equals(region2Pos1.getWorld())) return false;
    
    double minX1 = Math.min(region1Pos1.getX(), region1Pos2.getX());
    double maxX1 = Math.max(region1Pos1.getX(), region1Pos2.getX());
    double minY1 = Math.min(region1Pos1.getY(), region1Pos2.getY());
    double maxY1 = Math.max(region1Pos1.getY(), region1Pos2.getY());
    double minZ1 = Math.min(region1Pos1.getZ(), region1Pos2.getZ());
    double maxZ1 = Math.max(region1Pos1.getZ(), region1Pos2.getZ());
    
    double minX2 = Math.min(region2Pos1.getX(), region2Pos2.getX());
    double maxX2 = Math.max(region2Pos1.getX(), region2Pos2.getX());
    double minY2 = Math.min(region2Pos1.getY(), region2Pos2.getY());
    double maxY2 = Math.max(region2Pos1.getY(), region2Pos2.getY());
    double minZ2 = Math.min(region2Pos1.getZ(), region2Pos2.getZ());
    double maxZ2 = Math.max(region2Pos1.getZ(), region2Pos2.getZ());
    
    boolean xOverlap = maxX1 >= minX2 && maxX2 >= minX1;
    boolean yOverlap = maxY1 >= minY2 && maxY2 >= minY1;
    boolean zOverlap = maxZ1 >= minZ2 && maxZ2 >= minZ1;
    
    return xOverlap && yOverlap && zOverlap;
  }
  
  public static float getYawRotatedToGiven(float yaw) {
    return Location.normalizeYaw(Math.round(yaw / 90) * 90 - 180);
  }
  
  public static BlockFace getBlockFaceFromYaw(float yaw) {
    switch (Math.round((180 + yaw) / 90)) {
      case 0:
        return BlockFace.NORTH;
      case 1:
        return BlockFace.EAST;
      case 2:
        return BlockFace.SOUTH;
      case 3:
        return BlockFace.WEST;
      default:
        throw new IllegalStateException("Unexpected value: " + Math.round((180 + yaw) / 90));
    }
  }
  
  public static int packChunkCoords(@Range(from = 0, to = 15) int x,
                                    @Range(from = 0, to = 15) int z,
                                    @Range(from = -8388608, to = 8388607) int y) {
    return (x & 0xF) << 28 | (z & 0xF) << 24 | (y & 0xFFFFFF);
  }
  
  public static @Range(from = 0, to = 15)
  int unpackChunkX(int packed) {
    return (packed >> 28) & 0xF;
  }
  
  public static @Range(from = -8388608, to = 8388607)
  int unpackChunkY(int packed) {
    return (packed << 8) >> 8;
  }
  
  public static @Range(from = 0, to = 15)
  int unpackChunkZ(int packed) {
    return (packed >> 24) & 0xF;
  }
  
  public static int packChunkCoords(Location location) {
    int x = ((int) location.getX() - location.getChunk().getX() * 16);
    int z = ((int) location.getZ() - location.getChunk().getZ() * 16);
    int y = (int) location.getY();
    
    return packChunkCoords(x, z, y);
  }
  
  public static boolean validateAir(Location location) {
    if (location.getBlock().getType().isAir()) return true;
    
    ParticlesUtils.drawHollowCuboid(location, location.clone().add(1, 1, 1), Particle.ASH, 0.2);
    return false;
  }
  
  public static boolean validateSolid(Location location) {
    if (location.getBlock().getType().isSolid()) return true;
    
    ParticlesUtils.drawHollowCuboid(location, location.clone().add(1, 1, 1), Particle.ASH, 0.2);
    return false;
  }
  
  public static boolean validateNoEntities(Location pos1, Location pos2) {
    Collection<Entity> nearbyEntities = pos1.getWorld().getNearbyEntities(BoundingBox.of(pos1, pos2));
    
    if (!nearbyEntities.isEmpty()) {
      ParticlesUtils.drawHollowCuboid(pos1, pos2, Particle.ASH, 0.2);
      return false;
    }
    return true;
  }
  
  /**
   * Converts a location to a Vector containing the location's coordinates inside the chunk (rounded to int values)
   *
   * @param location Location
   * @return Vector containing the X, Y and Z int values of the location inside its chunk
   */
  public static Vector getCoordinatesInsideChunk(final Location location) {
    final int x = location.getBlockX() & 0x000F;
    final int y = location.getBlockY();
    final int z = location.getBlockZ() & 0x000F;
    return new Vector(x, y, z);
  }
}
