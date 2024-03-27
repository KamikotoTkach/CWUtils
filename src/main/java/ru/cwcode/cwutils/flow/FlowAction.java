package ru.cwcode.cwutils.flow;

public interface FlowAction<T> {
  void run(T object);
}
