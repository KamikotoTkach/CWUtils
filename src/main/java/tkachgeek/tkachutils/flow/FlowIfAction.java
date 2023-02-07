package tkachgeek.tkachutils.flow;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class FlowIfAction<T> implements FlowAction<T> {
  Consumer<T> action;
  Predicate<T> predicate;
  
  public FlowIfAction(Consumer<T> action, Predicate<T> predicate) {
    this.action = action;
    this.predicate = predicate;
  }
  
  @Override
  public void run(T object) {
    if (predicate != null) {
      if (predicate.test(object)) {
        action.accept(object);
      }
    }
  }
}
