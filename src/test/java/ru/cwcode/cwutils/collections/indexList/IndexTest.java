package ru.cwcode.cwutils.collections.indexList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class IndexTest {
  @Test
  public void testMapIndex() {
    IndexList<String> list = new IndexList<>(new ArrayList<>());
    MapIndex<Integer, String> lengthIndex = new MapIndex<>(new HashMap<>(), list, String::length);
    
    list.add("a");
    list.add("bb");
    list.add("ccc");
    list.add("ttt");
    
    Assertions.assertEquals("a", lengthIndex.get(1));
    Assertions.assertEquals("bb", lengthIndex.get(2));
    Assertions.assertEquals("ttt", lengthIndex.get(3));
  }
  
  @Test
  public void testMultiMapIndex() {
    IndexList<String> list = new IndexList<>(new ArrayList<>());
    MultiIndex<Integer, String> lengthIndex = new MultiIndex<>(list, String::length);
    
    list.add("a");
    list.add("bb");
    list.add("ccc");
    list.add("ttt");
    
    Assertions.assertTrue(lengthIndex.get(1).contains("a"));
    Assertions.assertTrue(lengthIndex.get(2).contains("bb"));
    List<String> strings = lengthIndex.get(3);
    Assertions.assertLinesMatch(strings, List.of("ccc", "ttt"));
  }
}