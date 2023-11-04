package tkachgeek.tkachutils.xp;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;

import java.util.Objects;

public class XpUtils {
  public static void dropExp(final Location location, final int xp) {
    final ExperienceOrb orb = (ExperienceOrb) Objects.requireNonNull(location.getWorld()).spawnEntity(location, EntityType.EXPERIENCE_ORB);
    orb.setExperience(xp);
  }
}
