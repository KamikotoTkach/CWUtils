package tkachgeek.tkachutils.datetime;

import java.io.Serializable;

public class Expireable implements Serializable {
  long time = 0;
  long expires = 0;
  
  public Expireable() {
  }
  
  public Expireable(long ms, long lastTime) {
    this(ms);
    time = lastTime;
  }
  
  public Expireable(long ms) {
    expires = ms;
  }
  
  public boolean isExpiredAndReset() {
    if (isExpired()) {
      reset();
      return true;
    }
    return false;
  }
  
  public boolean isExpired() {
    return time + expires < System.currentTimeMillis();
  }
  
  public void reset() {
    time = System.currentTimeMillis();
  }
  
  public void expireAfter(long ms) {
    expires = ms;
  }
  
  public long getLastTime() {
    return time;
  }
  
  public long getExpiresTime() {
    return expires;
  }
  
  public long getExpireAfterTime() {
    return (time + expires) - System.currentTimeMillis();
  }
  
  public double getPercent() {
    return (System.currentTimeMillis() - time) / (double) expires;
  }
}
