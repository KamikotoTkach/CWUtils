package tkachgeek.tkachutils.numbers;

public class NumbersUtils {

   public static final String[] suffix = new String[]{"", "k", "m", "b", "t"};

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
      int scale = (int) Math.pow(10, digits);
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
   public static int bound(int value, int min, int max) {
      return Math.max(Math.min(value, max), min);
   }

   /**
    * Устанавливает рамки числу (max)
    */
   public static int notGreater(int value, int max) {
      return Math.min(value, max);
   }

   /**
    * Устанавливает рамки числу (min)
    */
   public static int notLower(int value, int min) {
      return Math.max(value, min);
   }

   /**
    * Устанавливает рамки числу (min и max)
    */
   public static double bound(double value, double min, double max) {
      return Math.max(Math.min(value, max), min);
   }

   /**
    * Устанавливает рамки числу (max)
    */
   public static double notGreater(double value, double max) {
      return Math.min(value, max);
   }

   /**
    * Устанавливает рамки числу (min)
    */
   public static double notLower(double value, double min) {
      return Math.max(value, min);
   }

   public static String shortNumberFormat(double number) {
      int index = 0;

      while (number >= 1000) {
         number /= 1000;
         index++;
      }

      String formatted = String.format("%.1f", number).replace(',', '.');
      if (formatted.endsWith(".0")) {
         formatted = formatted.substring(0, formatted.length() - 2);
      }

      if (suffix.length <= index) return formatted + "?";

      return formatted + suffix[index];
   }

   /**
    * Возвращает абсолютное значение current относительно min и max
    */
   public static double absolute(double min, double max, double current) {
      return (current - min) / (max - min);
   }

   /**
    * Возвращает абсолютное значение current относительно min=0 и max
    */
   public static double absolute(double max, double current) {
      return current / max;
   }

   public static String format(int value) {
      String formatted = String.valueOf(value);

      int length = formatted.length();
      int count = (length - 1) / 3 + 1;
      String[] parts = new String[count];

      int begin = 0;
      int end = length % 3;
      end = end == 0 ? 3 : end;

      for (int k = 0; k < count; k++) {
         parts[k] = formatted.substring(begin, end);

         begin = end;
         end += 3;
      }

      return String.join(" ", parts);
   }
}
