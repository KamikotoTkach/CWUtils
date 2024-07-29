package ru.cwcode.cwutils.dependencyChecker;

import org.bukkit.plugin.Plugin;

import java.util.function.Function;
import java.util.function.Supplier;

public class RequirementAdapter<T> {
  Class<T> adapterClass;
  Function<Plugin, T> adapterFunction;
  Supplier<T> absentPluginAdapterFunction;
  
  public RequirementAdapter(Class<T> adapter,
                            Function<Plugin, T> adapterFunction,
                            Supplier<T> absentPluginAdapterFunction) {
    this.adapterClass = adapter;
    this.adapterFunction = adapterFunction;
    this.absentPluginAdapterFunction = absentPluginAdapterFunction;
  }
  
  public T adapt(Plugin plugin) {
    if (plugin == null) {
      return absentPluginAdapterFunction.get();
    }
    
    return adapterFunction.apply(plugin);
  }
  
  public Class<T> getAdapterClass() {
    return adapterClass;
  }
}
