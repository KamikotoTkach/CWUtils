package ru.cwcode.cwutils.text;

import java.util.ArrayList;
import java.util.List;

public class SpacesHider {
  /**
   * Заменяет пробелы в строке на нижнее подчёркивание
   */
  public static String hide(String original) {
    return original.replace(' ', '_');
  }
  
  /**
   * Заменяет пробелы в строках на нижнее подчёркивание
   */
  public static List<String> hide(List<String> original) {
    List<String> list = new ArrayList<>();
    for (String s : original) {
      list.add(hide(s));
    }
    return list;
  }
  
  /**
   * Заменяет нижнее подчёркивание на пробелы
   */
  public static String restore(String withHidedSpaces) {
    return withHidedSpaces.replace('_', ' ');
  }
  
  /**
   * Заменяет нижнее подчёркивание на пробелы
   */
  public static List<String> restore(List<String> withHidedSpaces) {
    List<String> list = new ArrayList<>();
    for (String s : withHidedSpaces) {
      list.add(restore(s));
    }
    return list;
  }
}
