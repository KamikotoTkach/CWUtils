package ru.cwcode.cwutils.text.converter;

public interface StringPrimitiveConverter<T> extends StringObjectConverter<T> {
  @Override
  default String convertToString(T t) {
    return t + "";
  }
  
  @Override
  default String convertToStringFancy(T t) {
    return t + "";
  }
}
