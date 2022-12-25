package tkachgeek.tkachutils.benchmark;

public class BenchmarkStage {
  String name;
  long started;
  long ended = -1;
  
  public BenchmarkStage(String name, Long started) {
    this.name = name;
    this.started = started;
  }
  
  public void stop(Long time) {
    ended = time;
  }
  
  public boolean isStarted() {
    return ended == -1;
  }
  
  public long getElapsedTime() {
    return ended-started;
  }
}