package ru.cwcode.cwutils.flow;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class FlowWhileAction<T> implements FlowAction<T> {
  Consumer<T> action;
  Predicate<T> predicate;
  
  public FlowWhileAction(Consumer<T> action, Predicate<T> predicate) {
    this.action = action;
    this.predicate = predicate;
  }
  
  public void run(T object) {
    if (predicate != null) {
      while (predicate.test(object)) {
        try {
          Thread.sleep(1);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
        action.accept(object);
      }
    }
  }
}
