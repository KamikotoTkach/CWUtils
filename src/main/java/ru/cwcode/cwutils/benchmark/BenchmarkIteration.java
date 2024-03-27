package ru.cwcode.cwutils.benchmark;

import java.util.ArrayList;
import java.util.List;

public class BenchmarkIteration {
  
  List<BenchmarkStage> stages = new ArrayList<>();
  
  public void newStage(String stage, Long time) {
    stopLastStage(time);
    addStage(stage, time);
  }
  
  public void stopLastStage(Long time) {
    if (stages.size() == 0) return;
    
    BenchmarkStage benchmarkStage = stages.get(stages.size() - 1);
    
    if (benchmarkStage.isStarted()) {
      benchmarkStage.stop(time);
    }
  }
  
  private void addStage(String stage, Long time) {
    stages.add(new BenchmarkStage(stage, time));
  }
}
