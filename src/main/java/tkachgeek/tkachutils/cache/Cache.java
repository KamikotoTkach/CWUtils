package tkachgeek.tkachutils.cache;

import java.time.Duration;
import java.util.function.Supplier;

public class Cache<T> {
  T value = null;
  long lastUpdateTime = 0;
  final Supplier<T> supplier;
  final Duration timeToUpdate;
  
  protected Cache(Supplier<T> supplier, Duration timeToUpdate) {
    this.supplier = supplier;
    this.timeToUpdate = timeToUpdate;
  }
  
  public static <T> Cache<T> of(Supplier<T> supplier, Duration timeToUpdate) {
    return new Cache<T>(supplier, timeToUpdate);
  }
  
  public void update() {
    value = supplier.get();
    lastUpdateTime = System.currentTimeMillis();
  }
  
  public T get() {
    if (System.currentTimeMillis() > lastUpdateTime + timeToUpdate.toMillis()) {
      update();
    }
    return value;
  }
}
