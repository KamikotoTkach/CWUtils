package ru.cwcode.cwutils.block;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BlockUtils {
  static List<Vector> directions = Arrays.stream(BlockFace.values())
                                         .filter(BlockFace::isCartesian)
                                         .map(BlockFace::getDirection)
                                         .collect(Collectors.toList());
  
  public static List<Location> neighboringLocations(Location location) {
    List<Location> ret = new ArrayList<>();
    for (Vector direction : directions) {
      ret.add(location.clone().add(direction));
    }
    return ret;
  }
  
  public static List<Block> neighboringBlocks(Block block) {
    List<Block> ret = new ArrayList<>();
    for (Vector direction : directions) {
      ret.add(block.getLocation().add(direction).getBlock());
    }
    return ret;
  }
  
  public static Location getCenter(final Block block) {
    return block.getLocation().add(0.5, 0.5, 0.5);
  }
}
