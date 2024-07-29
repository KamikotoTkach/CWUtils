package ru.cwcode.cwutils.dependencyChecker;

import java.util.ArrayList;
import java.util.List;

public class DependencyListeners {
  List<DependencyListener> listeners = new ArrayList<>();
  
  public void addDependencyListener(DependencyListener listener) {
    listeners.add(listener);
  }
  
  public void handleAdapter(Class<?> adapterClass, Object adapter) {
    listeners.forEach(x -> x.handleAdapter(adapterClass, adapter));
  }
}
