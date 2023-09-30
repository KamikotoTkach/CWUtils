package tkachgeek.tkachutils.entity;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class EntityUtils {
   public static Component getCustomName(@Nullable LivingEntity entity, Component defaultName) {
      if (entity == null) return defaultName;
      if (entity.customName() == null) return defaultName;

      return entity.customName();
   }
}
