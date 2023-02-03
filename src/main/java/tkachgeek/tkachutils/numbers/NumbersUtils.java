package tkachgeek.tkachutils.numbers;

public class NumbersUtils {
  
  /**
   * Округляет в меньшую сторону, но с шагом 0.5
   */
  public static double evalToHalf(double value) {
    return (int) value + (value - (int) value >= 0.5 ? 0.5 : 0.0);
  }
  
  /**
   * Умножает на 100 и округляет в меньшую сторону
   */
  public static int toPercent(double value) {
    return (int) (value * 100);
  }
  
  /**
   * @return умноженное на 100 и округлённое до нужного количества цифр (digits) после запятой
   */
  public static double toPercent(double value, int digits) {
    return round(value * 100, digits);
  }
  
  /**
   * @return округляет value до нужного количества цифр (digits) после запятой
   */
  public static double round(double value, int digits) {
    double scale = Math.pow(10, digits);
    return Math.ceil(value * scale) / scale;
  }
  
  /**
   * Проверяет является ли сторока числом double
   */
  public static boolean isNumber(String str) {
    return str.matches("^-?\\d+(?:\\.\\d+)?$");
  }
  
  /**
   * Проверяет является ли сторока числом integer
   */
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
  
  /**
   * Устанавливает рамки числу (min и max)
   */
  public static double bound(double max, double value, double min) {
    return Math.max(Math.min(value, max), min);
  }
  
  /**
   * Устанавливает рамки числу (max)
   */
  public static double notGreater(double max, double value) {
    return Math.min(value, max);
  }
  
  /**
   * Устанавливает рамки числу (min)
   */
  public static double notLower(double value, double min) {
    return Math.max(value, min);
  }
  
  /**
   * Возвращает абсолютное значение current относительно min и max
   */
  public double absolute(double min, double max, double current) {
    return (current - min) / (max - min);
  }
  
  /**
   * Возвращает абсолютное значение current относительно min=0 и max
   */
  public double absolute(double max, double current) {
    return current / max;
  }
}
