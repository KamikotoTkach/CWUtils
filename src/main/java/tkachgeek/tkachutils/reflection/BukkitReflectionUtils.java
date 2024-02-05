package tkachgeek.tkachutils.reflection;

import com.comphenix.protocol.reflect.FuzzyReflection;
import com.comphenix.protocol.utility.MinecraftReflection;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BukkitReflectionUtils {
  public static int getActiveWindowId(Player player) {
    try {
      FuzzyReflection fuzzy = FuzzyReflection.fromClass(MinecraftReflection.getCraftPlayerClass());
      Method getHandle = fuzzy.getMethodByName("getHandle");
      Object minecraftPlayer = getHandle.invoke(player);
      
      Field activeContainer = minecraftPlayer.getClass().getField("activeContainer");
      Object minecraftContainer = activeContainer.get(minecraftPlayer);
      
      Field windowId = minecraftContainer.getClass().getField("windowId");
      Object id = windowId.get(minecraftContainer);
      
      return (Integer) id;
    } catch (IllegalAccessException | NoSuchFieldException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }
}
