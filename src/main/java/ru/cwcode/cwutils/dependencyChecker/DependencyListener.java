package ru.cwcode.cwutils.dependencyChecker;

public interface DependencyListener {
  void handleAdapter(Class<?> adapterClass, Object adapter);
}
