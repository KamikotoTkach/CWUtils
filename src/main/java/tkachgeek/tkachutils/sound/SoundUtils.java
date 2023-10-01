package tkachgeek.tkachutils.sound;

import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;
import tkachgeek.tkachutils.collections.EnumUtils;

public class SoundUtils {
  public static Sound parse(@NotNull final String sound) {
    return EnumUtils.getEnumInstance(Sound.values(), sound).orElse(Sound.BLOCK_ANVIL_BREAK);
  }
}
