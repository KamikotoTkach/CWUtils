package tkachgeek.tkachutils.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class ItemStackUtils {
  @SuppressWarnings("deprecation")
  public static @Nullable Material fromLegacyID(int ID, byte Data) {
    for (Material i : EnumSet.allOf(Material.class))
      if (i.getId() == ID) return Bukkit.getUnsafe().fromLegacy(new MaterialData(i, Data));
    return null;
  }
}
