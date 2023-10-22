package tkachgeek.tkachutils.entity;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class EntityUtils {
  public static Component getCustomName(@Nullable LivingEntity entity, Component defaultName) {
    return (entity == null || entity.customName() == null) ? defaultName : entity.customName();
  }
}
