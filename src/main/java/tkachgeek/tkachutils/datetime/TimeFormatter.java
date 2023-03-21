package tkachgeek.tkachutils.datetime;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeFormatter {
  private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("mm:ss");
  
  public static String format(long millis, String format) {
    SimpleDateFormat dateFormat = new SimpleDateFormat(format);
    return dateFormat.format(new Date(millis));
  }
  
  public static String format(long millis) {
    return SIMPLE_DATE_FORMAT.format(new Date(millis));
  }
}
