package tkachgeek.tkachutils.flow;

public interface FlowAction<T> {
  void run(T object);
}
