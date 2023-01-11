package tkachgeek.tkachutils.text;

import java.util.ArrayList;
import java.util.List;

public class SpacesHider {
  public static String hide(String original) {
    return original.replace(' ', '_');
  }
  
  public static List<String> hide(List<String> original) {
    List<String> list = new ArrayList<>();
    for (String s : original) {
      list.add(hide(s));
    }
    return list;
  }
  
  public static String restore(String withHidedSpaces) {
    return withHidedSpaces.replace('_', ' ');
  }
  
  public static List<String> restore(List<String> withHidedSpaces) {
    List<String> list = new ArrayList<>();
    for (String s : withHidedSpaces) {
      list.add(restore(s));
    }
    return list;
  }
}
