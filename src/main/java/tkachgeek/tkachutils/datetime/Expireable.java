package tkachgeek.tkachutils.datetime;

import java.io.Serializable;

public class Expireable implements Serializable {
  long time = 0;
  long expires = 0;
  
  public Expireable() {
  }
  
  /**
   * @param ms       to expire
   * @param lastTime - countdown time expires from
   */
  public Expireable(long ms, long lastTime) {
    this(ms);
    time = lastTime;
  }
  
  /**
   * @param ms to expire
   */
  public Expireable(long ms) {
    expires = ms;
  }
  
  /**
   * isExpired() and reset()
   */
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
  
  /**
   * @return countdown time expires from
   */
  public long getLastTime() {
    return time;
  }
  
  /**
   * @return ms to expire (absolute)
   */
  public long getExpiresTime() {
    return expires;
  }
  
  /**
   * @return ms to expiry (relative to current time)
   */
  public long getExpireAfterTime() {
    return (time + expires) - System.currentTimeMillis();
  }
  
  /**
   * @return percent of expiring, >=1 if expired, >0 && <1 if not expired
   */
  public double getPercent() {
    return (System.currentTimeMillis() - time) / (double) expires;
  }
  
  /**
   * @return 1 if expired, >0 && <1 if not expired
   */
  public double getPercentBounded() {
    return Math.min(1.0, (System.currentTimeMillis() - time) / (double) expires);
  }
  
  /**
   * @return 0 if expired, >0 && <1 if not expired
   */
  public double getRevertPercentBounded() {
    return 1 - getPercentBounded();
  }
}
