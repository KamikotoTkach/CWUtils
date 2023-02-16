package tkachgeek.tkachutils.conditions;

public class Conditions {
  /**
   *
   * @param toCheck значение для проверки
   * @param ifNull значение, возвращаемое, если первое - null
   * @return первое значение, если оно не null, иначе второе
   */
  public static <T> T checkNull(T toCheck, T ifNull) {
    return toCheck == null ? ifNull : toCheck;
  }
  
  /**
   * @param values значения для проверки
   * @return первый не-null-евой объект, иначе null
   */
  public static <T> T selectNotNull(T[] values) {
    for (T value : values) {
      if (value != null) {
        return value;
      }
    }
    return null;
  }
}
