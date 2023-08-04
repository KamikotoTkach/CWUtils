package tkachgeek.tkachutils.collections;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Set;

public class ExpiredSet<T> {
  private final HashMap<T, LocalDateTime> expired = new HashMap<>();
  
  public void setExpired(T element, Duration duration) {
    this.expired.put(element, LocalDateTime.now().plus(duration.toMillis(), ChronoUnit.MILLIS));
  }
  
  public boolean isActive(T element) {
    return this.getStatus(element).equals(Status.ACTIVE);
  }
  
  public Status getStatus(T element) {
    LocalDateTime timeToExpired = this.expired.get(element);
    
    if (timeToExpired == null) {
      return Status.NO_ELEMENT;
    }
    
    return LocalDateTime.now().isBefore(timeToExpired) ? Status.ACTIVE : Status.EXPIRED;
  }
  
  public boolean has(T expired) {
    return this.expired.containsKey(expired);
  }
  
  public Set<T> getExpired() {
    return this.expired.keySet();
  }
  
  public HashMap<T, LocalDateTime> entries() {
    return this.expired;
  }
  
  public enum Status {
    ACTIVE,
    EXPIRED,
    NO_ELEMENT
  }
}
