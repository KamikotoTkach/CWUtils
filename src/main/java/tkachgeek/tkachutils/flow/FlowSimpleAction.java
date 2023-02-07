package tkachgeek.tkachutils.flow;

import java.util.function.Consumer;

public class FlowSimpleAction<T> implements FlowAction<T> {
  Consumer<T> action;
  
  public FlowSimpleAction(Consumer<T> action) {
    this.action = action;
  }
  
  @Override
  public void run(T object) {
    action.accept(object);
  }
}
