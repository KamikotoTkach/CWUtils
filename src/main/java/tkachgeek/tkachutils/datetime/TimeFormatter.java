package tkachgeek.tkachutils.datetime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeFormatter {
  private static final SimpleDateFormat MM_SS_TIME_FORMAT = new SimpleDateFormat("mm:ss");
  private static final SimpleDateFormat HH_MM_SS_TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
  private static final SimpleDateFormat MM_SS_TIME_FORMAT_UTC = new SimpleDateFormat("mm:ss");
  private static final SimpleDateFormat HH_MM_SS_TIME_FORMAT_UTC = new SimpleDateFormat("HH:mm:ss");
  private static final SimpleDateFormat FULL_DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
  
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
