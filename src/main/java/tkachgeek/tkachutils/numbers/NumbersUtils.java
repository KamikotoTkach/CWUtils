package tkachgeek.tkachutils.numbers;

public class NumbersUtils {
  
  public static double evalToHalf(double value) {
    return (int) value + (value - (int) value >= 0.5 ? 0.5 : 0.0);
  }
  
  public static int toPercent(double value) {
    return (int) (value * 100);
  }
  
  public static double toPercent(double value, int digits) {
    return round(value * 100, digits);
  }
  
  public static double round(double value, int digits) {
    double scale = Math.pow(10, digits);
    return Math.ceil(value * scale) / scale;
  }
  public static boolean isNumber(String str) {
    return str.matches("^-?\\d+(?:\\.\\d+)?$");
  }
  public static boolean isInteger(String str) {
    if (str == null)
      return false;
    char[] data = str.toCharArray();
    if (data.length == 0)
      return false;
    int index = 0;
    if (data[0] == '-' && data.length > 1)
      index = 1;
    for (; index < data.length; index++) {
      if (data[index] < '0' || data[index] > '9')
        return false;
    }
    return true;
  }
}
