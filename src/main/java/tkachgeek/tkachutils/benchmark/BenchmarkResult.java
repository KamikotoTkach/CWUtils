package tkachgeek.tkachutils.benchmark;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.LongStream;

public class BenchmarkResult {
  
  public static HashMap<String, StageResult> calculate(List<BenchmarkIteration> iterations) {
    LinkedHashMap<String, StageResult> resultMap = new LinkedHashMap<>();
    
    for (BenchmarkIteration iteration : iterations) {
      
      for (BenchmarkStage stage : iteration.stages) {
        
        if (!resultMap.containsKey(stage.name)) {
          resultMap.put(stage.name, new StageResult());
        }
        if(stage.getElapsedTime()>0) resultMap.get(stage.name).add(stage.getElapsedTime());
      }
    }
    return resultMap;
  }
  
  static class StageResult {
    List<Long> elapsed = new ArrayList<>();
    
    public long getMax() {
      return elapsed.stream().flatMapToLong(LongStream::of).max().orElse(-1L);
    }
    
    public long getMin() {
      return elapsed.stream().flatMapToLong(LongStream::of).min().orElse(-1L);
    }
    
    public double getAvg() {
      return elapsed.stream().flatMapToLong(LongStream::of).average().orElse(-1D);
    }
    
    public int getStarts() {
      return elapsed.size();
    }
    
    public void add(long elapsedTime) {
      elapsed.add(elapsedTime);
    }
  }
}