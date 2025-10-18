package ru.cwcode.cwutils.datetime;

import java.util.Calendar;

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
  
  public static boolean isSameDay(long millis1, long millis2) {
    Calendar cal1 = Calendar.getInstance();
    cal1.setTimeInMillis(millis1);
    
    Calendar cal2 = Calendar.getInstance();
    cal2.setTimeInMillis(millis2);
    
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
           && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
           && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
  }
}
