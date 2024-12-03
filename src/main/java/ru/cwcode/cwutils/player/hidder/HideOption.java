package ru.cwcode.cwutils.player.hidder;


import java.time.Duration;
import java.util.Objects;

public class HideOption {
  private HideType type;
  private long expire;
  
  protected HideOption() {
  }
  
  public HideOption(HideType type, long expire) {
    this.type = type;
    this.expire = expire;
  }
  
  public HideOption(HideType type, Duration duration) {
    this(type, System.currentTimeMillis() + duration.toMillis());
  }
  
  public HideType getType() {
    return type;
  }
  
  public boolean isExpired() {
    return System.currentTimeMillis() > expire;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (HideOption) obj;
    return Objects.equals(this.type, that.type) &&
           this.expire == that.expire;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(type, expire);
  }
  
  @Override
  public String toString() {
    return "HideOption[" +
           "type=" + type + ", " +
           "expire=" + expire + ']';
  }
}
