package ru.cwcode.cwutils.dependencyChecker;

import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class DependencyChecker {
  List<PluginRequirement> requirements = new ArrayList<>();
  DependencyListeners listeners = new DependencyListeners();
  
  public DependencyChecker addDependency(String pluginId, Consumer<PluginRequirement.PluginRequirementBuilder> builder) {
    PluginRequirement.PluginRequirementBuilder requirementBuilder = PluginRequirement.of(pluginId);
    
    builder.accept(requirementBuilder);
    
    requirements.add(requirementBuilder.build());
    
    return this;
  }
  
  public DependencyChecker addListener(DependencyListener listener) {
    this.listeners.addDependencyListener(listener);
    
    return this;
  }
  
  public void handleDependencies() {
    requirements.stream().peek(x -> {
                  Optional<Plugin> requiredPlugin = x.getRequiredPlugin();
                  
                  if (!x.isOptional() && requiredPlugin.isEmpty()) {
                    throw new UnsatisfiedDependencyException(x);
                  }
                })
                .forEach(requirement -> {
                  Optional<Plugin> requiredPlugin = requirement.getRequiredPlugin();
                  
                  requirement.adaptPlugin(requiredPlugin.orElse(null))
                             .ifPresent(adapter -> listeners.handleAdapter(requirement.getRequirementAdapter().getAdapterClass(), adapter));
                });
  }
}
