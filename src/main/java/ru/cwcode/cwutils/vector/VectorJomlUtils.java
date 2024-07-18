package ru.cwcode.cwutils.vector;

import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;
import org.joml.Vector2f;

public class VectorJomlUtils {
  public static Vector2f flatter(BlockFace facing, Vector point) {
    
    switch (facing) {
      case NORTH:
        return new Vector2f((float) point.getX(), (float) point.getY());
      case EAST:
        return new Vector2f((float) point.getZ(), (float) point.getY());
      case SOUTH:
        return new Vector2f((float) -point.getX() + 1, (float) point.getY());
      case WEST:
        return new Vector2f((float) -point.getZ() + 1, (float) point.getY());
      case UP:
        return new Vector2f((float) point.getX(), (float) point.getZ());
      case DOWN:
        return new Vector2f((float) point.getX(), (float) -point.getZ() + 1);
    }
    
    throw new IllegalStateException("BlockFace is not cartesian");
  }
}
