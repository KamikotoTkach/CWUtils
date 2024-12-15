package ru.cwcode.cwutils.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class EventHandler<E extends Event> implements Listener {
  
  private boolean isUnregister = false;
  
  public EventHandler(Class<E> eventClass, TemporaryEvent<E> event, @NotNull Plugin plugin) {
    this(eventClass, EventPriority.NORMAL, plugin, event);
  }
  
  public EventHandler(Class<E> eventClass, EventPriority priority, @NotNull Plugin plugin, TemporaryEvent<E> event) {
    Bukkit.getServer().getPluginManager().registerEvent(eventClass, this, priority,
                                                        (ignored, ev) -> {
                                                          try {
                                                            event.use((E) ev);
                                                          } catch (ClassCastException e) {
                                                          }
                                                        },
                                                        
                                                        plugin);
  }
  
  public EventHandler(Class<E> eventClass, @NotNull Plugin plugin, TemporaryEventAutoUnregister<E> event) {
    this(eventClass, EventPriority.NORMAL, plugin, event);
  }
  
  public EventHandler(Class<E> eventClass, EventPriority priority, @NotNull Plugin plugin, TemporaryEventAutoUnregister<E> event) {
    Bukkit.getServer().getPluginManager().registerEvent(eventClass, this, priority,
                                                        (ignored, ev) -> {
                                                          try {
                                                            if (isUnregister) return;
                                                            
                                                            unregister(event.use((E) ev));
                                                          } catch (ClassCastException e) {
                                                          }
                                                        }, plugin);
  }
  
  public void unregister(boolean bool) {
    if (bool) unregister();
  }
  
  public void unregister() {
    HandlerList.unregisterAll(this);
    isUnregister = true;
  }
  
  public boolean isUnregister() {
    return isUnregister;
  }
  
  public interface TemporaryEvent<E extends Event> {
    
    void use(E event);
  }
  
  public interface TemporaryEventAutoUnregister<E extends Event> {
    
    /**
     * Return true if you want to remove the listener
     */
    boolean use(E event);
  }
}