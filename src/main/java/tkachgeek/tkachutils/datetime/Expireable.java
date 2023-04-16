package tkachgeek.tkachutils.datetime;

public class Expireable {
  long time = 0;
  long expires = 0;
  
  public Expireable() {
  }
  
  public Expireable(long ms) {
    expires = ms;
  }
  
  public Expireable(long ms, long lastTime) {
    this(ms);
    time = lastTime;
  }
  
  public boolean isExpired() {
    return time + expires < System.currentTimeMillis();
  }
  
  public boolean isExpiredAndReset() {
    if (isExpired()) {
      reset();
      return true;
    }
    return false;
  }
  
  public void reset() {
    time = System.currentTimeMillis();
  }
  
  public void expireAfter(long ms) {
    expires = ms;
  }
}
