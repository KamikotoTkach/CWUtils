package ru.cwcode.cwutils.datetime;

public class TimeUtils {
  public static long unix() {
    return System.currentTimeMillis() / 1000;
  }
  
  public static boolean isBefore(long millis) {
    return System.currentTimeMillis() < millis;
  }
  
  public static boolean isAfter(long millis) {
    return System.currentTimeMillis() > millis;
  }
}
