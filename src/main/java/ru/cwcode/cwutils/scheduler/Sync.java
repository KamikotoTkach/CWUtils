package ru.cwcode.cwutils.scheduler;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Supplier;

public class Sync {
  @SneakyThrows
  public static <T> T get(JavaPlugin plugin, Supplier<T> runnable) {
    if (Bukkit.isPrimaryThread()) {
      return runnable.get();
    } else {
      return Bukkit.getScheduler().callSyncMethod(plugin, runnable::get).get();
    }
  }
  
  public static void run(JavaPlugin plugin, Runnable runnable) {
    get(plugin, () -> {
      runnable.run();
      return null;
    });
  }
}
