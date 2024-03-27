package ru.cwcode.cwutils.bootstrap;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Bootstrap extends JavaPlugin {
  CompletableFuture<Void> loadTask;
  
  @Override
  public void onLoad() {
    loadTask = asyncTask();
  }
  
  @Override
  public void onEnable() {
    try {
      if (loadTask != null) loadTask.get();
      loadTask = null;
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
    
    onPostEnable();
  }
  
  public void onPostEnable() {
  
  }
  
  protected CompletableFuture<Void> asyncTask() {
    return null;
  }
}
