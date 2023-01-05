package tkachgeek.tkachutils;

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
                                         .map(BlockFace::getDirection)
                                         .filter(x -> Math.abs(x.getBlockX()) + Math.abs(x.getBlockY()) + Math.abs(x.getBlockZ()) == 1)
                                         .collect(Collectors.toList());
  
  static List<Location> neighboringLocations(Location location) {
    List<Location> ret = new ArrayList<>();
    for (Vector direction : directions) {
      ret.add(location.clone().add(direction));
    }
    return ret;
  }
  
  static List<Block> neighboringBlocks(Block block) {
    List<Block> ret = new ArrayList<>();
    for (Vector direction : directions) {
      ret.add(block.getLocation().add(direction).getBlock());
    }
    return ret;
  }
}
