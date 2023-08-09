package tkachgeek.tkachutils.particles;

import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class ParticlesUtils {
   public static ParticleBuilder getRedstoneParticle(Color color, int size) {
      return Particle.REDSTONE.builder()
                              .count(0)
                              .extra(1)
                              .color(color, size);
   }

   public static void drawLineParticles(Location start, Location end, Particle particle, double particleSpacing) {
      drawLineParticles(start, end, particle.builder(), particleSpacing);
   }

   public static void drawLineParticles(Location start, Location end, ParticleBuilder particle, double particleSpacing) {
      Vector direction = end.toVector().subtract(start.toVector()).normalize();
      double distance = start.distance(end);
      int numParticles = (int) Math.ceil(distance / particleSpacing);

      for (int i = 0; i < numParticles; i++) {
         Location particleLocation = start.clone().add(direction.clone().multiply(particleSpacing * i));
         particle.location(particleLocation)
                 .spawn();
      }
   }

   public static void drawHollowCuboid(Location corner1, Location corner2, Particle particle, double particleSpacing) {
      drawHollowCuboid(corner1, corner2, particle.builder(), particleSpacing);
   }

   public static void drawHollowCuboid(Location corner1, Location corner2, ParticleBuilder particle, double particleSpacing) {
      double minX = Math.min(corner1.getX(), corner2.getX());
      double minY = Math.min(corner1.getY(), corner2.getY());
      double minZ = Math.min(corner1.getZ(), corner2.getZ());

      double maxX = Math.max(corner1.getX(), corner2.getX());
      double maxY = Math.max(corner1.getY(), corner2.getY());
      double maxZ = Math.max(corner1.getZ(), corner2.getZ());

      World world = corner1.getWorld();

      for (double x = minX; x <= maxX; x += particleSpacing) {
         drawLineParticles(new Location(world, x, minY, minZ), new Location(world, x, maxY, minZ), particle, particleSpacing);
         drawLineParticles(new Location(world, x, minY, maxZ), new Location(world, x, maxY, maxZ), particle, particleSpacing);

         drawLineParticles(new Location(world, x, minY, minZ), new Location(world, x, minY, maxZ), particle, particleSpacing);
         drawLineParticles(new Location(world, x, maxY, minZ), new Location(world, x, maxY, maxZ), particle, particleSpacing);
      }

      for (double z = minZ; z <= maxZ; z += particleSpacing) {
         drawLineParticles(new Location(world, minX, minY, z), new Location(world, minX, maxY, z), particle, particleSpacing);
         drawLineParticles(new Location(world, maxX, minY, z), new Location(world, maxX, maxY, z), particle, particleSpacing);
      }
   }
}
