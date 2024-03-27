package ru.cwcode.cwutils.benchmark;

import java.util.HashMap;

public class Benchmark {
  static HashMap<String, BenchmarkChannel> benchmarks = new HashMap<>();
  static int iterationCount = 200;
  static boolean enabled = true;
  
  public static void stopAt(int iterationCount) {
    Benchmark.iterationCount = iterationCount;
  }
  
  public static void enable() {
    enabled = true;
  }
  
  public static void disable() {
    enabled = false;
  }
  
  public static BenchmarkChannel getChannel(String name) {
    if (!benchmarks.containsKey(name)) benchmarks.put(name, new BenchmarkChannel());
    return benchmarks.get(name);
  }
  
  public static void stage(String channel, String stage) {
    if (!enabled) return;
    
    long time = System.nanoTime();
    getChannel(channel).newStage(stage, time);
  }
  
  public static void newIteration(String channel) {
    if (!enabled) return;
    
    long time = System.nanoTime();
    stop(channel, time);
    
    if (iterationCount < getChannel(channel).iterations.size()) {
      Benchmark.print(channel);
      return;
    }
    
    getChannel(channel).newIteration(time);
  }
  
  public static void print(String channel) {
    if (!enabled) return;
    
    getChannel(channel).print(channel);
    removeChannel(channel);
  }
  
  protected static void stop(String channel, long time) {
    if (!enabled) return;
    
    getChannel(channel).stopLastStage(time);
  }
  
  private static void removeChannel(String channel) {
    if (!enabled) return;
    
    benchmarks.remove(channel);
  }
}