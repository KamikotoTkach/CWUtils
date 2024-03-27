package ru.cwcode.cwutils.datetime;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.TimeZone;

public class TimeFormatter {
  public static final SimpleDateFormat MM_SS_TIME_FORMAT = new SimpleDateFormat("mm:ss");
  public static final SimpleDateFormat HH_MM_SS_TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
  public static final SimpleDateFormat MM_SS_TIME_FORMAT_UTC = new SimpleDateFormat("mm:ss");
  public static final SimpleDateFormat HH_MM_SS_TIME_FORMAT_UTC = new SimpleDateFormat("HH:mm:ss");
  public static final SimpleDateFormat FULL_DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
  private static final String formatted_time_days = "д";
  private static final String formatted_time_hours = "ч";
  private static final String formatted_time_minutes = "мин";
  private static final String formatted_time_seconds = "сек";
  
  static {
    MM_SS_TIME_FORMAT_UTC.setTimeZone(TimeZone.getTimeZone("UTC"));
    HH_MM_SS_TIME_FORMAT_UTC.setTimeZone(TimeZone.getTimeZone("UTC"));
  }
  
  public static String formatFull(long millis) {
    return format(millis, FULL_DATE_FORMAT);
  }
  
  public static String formatHours(long millis) {
    return format(millis, HH_MM_SS_TIME_FORMAT);
  }
  
  public static String format(long millis, SimpleDateFormat format) {
    return format.format(millis);
  }
  
  public static String formatHoursUTC(long millis) {
    return format(millis, HH_MM_SS_TIME_FORMAT_UTC);
  }
  
  public static String formatNowFull() {
    return format(System.currentTimeMillis(), FULL_DATE_FORMAT);
  }
  
  public static String formatNowHours() {
    return format(System.currentTimeMillis(), HH_MM_SS_TIME_FORMAT);
  }
  
  public static String formatNow(String format) {
    return format(System.currentTimeMillis(), format);
  }
  
  public static String format(long millis, String format) {
    SimpleDateFormat dateFormat = new SimpleDateFormat(format);
    return dateFormat.format(new Date(millis));
  }
  
  public static String formatNow() {
    return format(System.currentTimeMillis());
  }
  
  public static String format(long millis) {
    return format(millis, MM_SS_TIME_FORMAT);
  }
  
  public static String formatUTC(long millis) {
    return format(millis, MM_SS_TIME_FORMAT_UTC);
  }
  
  public static String getFormattedTime(@NotNull final Duration duration, int timeParamsCount, String formatted_time_days, String formatted_time_hours, String formatted_time_minutes, String formatted_time_seconds) {
    StringBuilder formattedTime = new StringBuilder();
    
    long days = Math.abs(duration.toDaysPart());
    long hours = Math.abs(duration.toHoursPart());
    long minutes = Math.abs(duration.toMinutesPart());
    long seconds = Math.abs(duration.toSecondsPart());
    
    if (seconds == 0) seconds = 1;
    
    if (days > 0) {
      formattedTime.append(days).append(formatted_time_days);
      timeParamsCount--;
    }
    
    if (hours > 0 && timeParamsCount > 0) {
      if (formattedTime.length() > 0) formattedTime.append(" ");
      formattedTime.append(hours).append(formatted_time_hours);
      timeParamsCount--;
    }
    
    if (minutes > 0 && timeParamsCount > 0) {
      if (formattedTime.length() > 0) formattedTime.append(" ");
      formattedTime.append(minutes).append(formatted_time_minutes);
      timeParamsCount--;
    }
    
    if (seconds > 0 && timeParamsCount > 0) {
      if (formattedTime.length() > 0) formattedTime.append(" ");
      formattedTime.append(seconds).append(formatted_time_seconds);
    }
    
    return formattedTime.toString();
  }
  
  public static String getFormattedTime(@NotNull final Duration duration, int timeParamsCount) {
    return TimeFormatter.getFormattedTime(
       duration,
       timeParamsCount,
       TimeFormatter.formatted_time_days,
       TimeFormatter.formatted_time_hours,
       TimeFormatter.formatted_time_minutes,
       TimeFormatter.formatted_time_seconds
    );
  }
  
  public static String getFormattedTime(@NotNull final Duration duration) {
    return TimeFormatter.getFormattedTime(duration, 2);
  }
  
  /**
   * Moved to TimeUtils
   */
  @Deprecated(forRemoval = true)
  public static long unix() {
    return System.currentTimeMillis() / 1000;
  }
  
  /**
   * Moved to TimeUtils
   */
  @Deprecated(forRemoval = true)
  public static boolean isBefore(long millis) {
    return System.currentTimeMillis() < millis;
  }
  
  /**
   * Moved to TimeUtils
   */
  @Deprecated(forRemoval = true)
  public static boolean isAfter(long millis) {
    return System.currentTimeMillis() > millis;
  }
}
