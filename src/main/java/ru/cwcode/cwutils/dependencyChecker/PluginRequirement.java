package ru.cwcode.cwutils.dependencyChecker;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.cwcode.cwutils.server.ServerUtils;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class PluginRequirement {
  private final String pluginId;
  private final String versionMin;
  private final String versionMax;
  private final boolean optional;
  
  private final RequirementAdapter<?> requirementAdapter;
  
  PluginRequirement(String pluginId,
                    String versionMin,
                    String versionMax,
                    boolean optional,
                    @Nullable RequirementAdapter<?> requirementAdapter) {
    this.pluginId = pluginId;
    this.versionMin = versionMin;
    this.versionMax = versionMax;
    this.optional = optional;
    this.requirementAdapter = requirementAdapter;
  }
  
  public static PluginRequirementBuilder of(String pluginId) {
    return new PluginRequirementBuilder(pluginId);
  }
  
  @Override
  public String toString() {
    return "PluginRequirement{" +
           "pluginId='" + pluginId + '\'' +
           ", versionMin='" + versionMin + '\'' +
           ", versionMax='" + versionMax + '\'' +
           ", optional=" + optional +
           '}';
  }
  
  public Optional<Plugin> getRequiredPlugin() {
    return Optional.ofNullable(Bukkit.getPluginManager().getPlugin(pluginId))
                   .filter(this::isVersionAcceptable);
  }
  
  public String getProvidedVersion() {
    return getPlugin()
      .map(x -> x.getDescription().getVersion())
      .orElse("absent");
  }
  
  public Optional<Object> adaptPlugin(Plugin plugin) {
    if (requirementAdapter == null) return Optional.empty();
    
    return Optional.ofNullable(requirementAdapter.adapt(plugin));
  }
  
  public int versionMinNum() {
    return ServerUtils.getVersionWeight(versionMin);
  }
  
  public int versionMaxNum() {
    return ServerUtils.getVersionWeight(versionMax);
  }
  
  public boolean isOptional() {
    return optional;
  }
  
  public RequirementAdapter<?> getRequirementAdapter() {
    return requirementAdapter;
  }
  
  private @NotNull Optional<Plugin> getPlugin() {
    return Optional.ofNullable(Bukkit.getPluginManager().getPlugin(pluginId));
  }
  
  private boolean isVersionAcceptable(Plugin plugin) {
    int currentVersion = ServerUtils.getVersionWeight(plugin.getDescription().getVersion());
    return (versionMinNum() == 0 || currentVersion >= versionMinNum())
           && (versionMaxNum() == 0 || currentVersion <= versionMaxNum());
  }
  
  public static class PluginRequirementBuilder {
    private final String pluginId;
    private String versionMin = "any";
    private String versionMax = "any";
    private boolean optional = false;
    private RequirementAdapter<?> requirementAdapter;
    
    PluginRequirementBuilder(String pluginId) {
      this.pluginId = pluginId;
    }
    
    public PluginRequirementBuilder versionMin(String versionMin) {
      this.versionMin = versionMin;
      return this;
    }
    
    public PluginRequirementBuilder versionMax(String versionMax) {
      this.versionMax = versionMax;
      return this;
    }
    
    public PluginRequirementBuilder optional(boolean optional) {
      this.optional = optional;
      return this;
    }
    
    public PluginRequirementBuilder optional() {
      return optional(true);
    }
    
    public <T> PluginRequirementBuilder adapter(Class<T> clazz,
                                                Function<Plugin, T> adapter,
                                                Supplier<T> absentPluginAdapter) {
      
      requirementAdapter = new RequirementAdapter<>(clazz, adapter, absentPluginAdapter);
      
      return this;
    }
    
    public <T> PluginRequirementBuilder adapter(Class<T> clazz,
                                                Function<Plugin, T> adapter) {
      
      requirementAdapter = new RequirementAdapter<>(clazz, adapter, () -> {
        throw new NoSuchElementException(String.format("Plugin %s is required", pluginId));
      });
      
      return this;
    }
    
    public PluginRequirement build() {
      return new PluginRequirement(this.pluginId, this.versionMin, this.versionMax, optional, requirementAdapter);
    }
  }
}
