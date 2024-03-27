package ru.cwcode.cwutils.benchmark;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

public class BenchmarkChannel {
  List<BenchmarkIteration> iterations = new ArrayList<>();
  
  public void print(String channel) {
    Logger log = Logger.getLogger(channel);
    var result = BenchmarkResult.calculate(iterations);
    var stagesMaxNameLen = Math.max(8, result.keySet()
                                             .stream()
                                             .max(Comparator.comparingInt(String::length))
                                             .get()
                                             .length());
    
    AtomicLong max = new AtomicLong(-1);
    AtomicLong min = new AtomicLong(-1);
    AtomicLong avg = new AtomicLong(-1);
    
    log.warning("Бенчмарк завершён");
    log.warning(String.format("Итераций всего: %s", iterations.size()));
    log.warning(String.format("%" + stagesMaxNameLen + "s: %10s %12s %12s %s", "Стадия", "Минимум", "Среднее", "Максимум", "Запусков"));
    
    result.forEach((stage, value) -> {
      max.addAndGet(value.getMax());
      min.addAndGet(value.getMin());
      avg.addAndGet((long) value.getAvg());
      log.warning(String.format("%" + stagesMaxNameLen + "s: %10d %12d %12d %8d", stage, value.getMin(), (long) value.getAvg(), value.getMax(), value.getStarts()));
    });
    
    log.warning(String.format("%" + stagesMaxNameLen + "s: %10d %12d %12d", "ИТОГО:", min.get(), avg.get(), max.get()));
  }
  
  public void newIteration(long time) {
    lastIteration().stopLastStage(time);
    addIteration();
  }
  
  private void addIteration() {
    iterations.add(new BenchmarkIteration());
  }
  
  private BenchmarkIteration lastIteration() {
    if (iterations.size() == 0) addIteration();
    return iterations.get(iterations.size() - 1);
  }
  
  public void newStage(String stage, long time) {
    lastIteration().newStage(stage, time);
  }
  
  public void stopLastStage(long time) {
    lastIteration().stopLastStage(time);
  }
}