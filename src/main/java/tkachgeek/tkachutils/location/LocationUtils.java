package tkachgeek.tkachutils.location;

import org.bukkit.Location;

import java.util.Random;

public class LocationUtils {
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

   public static Location randomLocation(Location pos1, Location pos2) {
      int x1 = Math.min(pos1.getBlockX(), pos2.getBlockX());
      int y1 = Math.min(pos1.getBlockY(), pos2.getBlockY());
      int z1 = Math.min(pos1.getBlockZ(), pos2.getBlockZ());

      int x2 = Math.max(pos1.getBlockX(), pos2.getBlockX());
      int y2 = Math.max(pos1.getBlockY(), pos2.getBlockY());
      int z2 = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

      Random random = new Random();
      Location location = new Location(
            pos1.getWorld(),
            random.nextInt(x2 - x1 + 1) + x1,
            random.nextInt(y2 - y1 + 1) + y1,
            random.nextInt(z2 - z1 + 1) + z1
      );

      return location;
   }
}
