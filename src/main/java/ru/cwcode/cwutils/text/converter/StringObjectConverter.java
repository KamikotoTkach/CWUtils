package ru.cwcode.cwutils.text.converter;

public interface StringObjectConverter<T> {
  String convertToString(T t);
  
  String convertToStringFancy(T t);
  
  T convertFromString(String s);
}
