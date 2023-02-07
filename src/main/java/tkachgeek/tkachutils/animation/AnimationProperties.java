package tkachgeek.tkachutils.animation;

import org.bukkit.Bukkit;
import tkachgeek.minemation.animation.timingFunction.TimingFunction;
import tkachgeek.minemation.animation.timingFunction.TimingFunctions;

import java.time.Duration;

public class AnimationProperties {
  final double start, end;
  final Duration duration;
  long frames, currentFrame = -1;
  double step, frameDelay;
  boolean animationEnded = false;
  TimingFunction timingFunction = TimingFunctions.linear;
  boolean reversed = false;
  
  public AnimationProperties(double start, double end, Duration duration) {
    this.start = start;
    this.end = end;
    this.duration = duration;
    
    setFrames(duration.toMillis() / 50);
    if (start > end) reversed();
  }
  
  public AnimationProperties setFrames(long frames) {
    this.frames = frames;
    this.step = Math.abs((end - start) / (double) frames);
    this.frameDelay = duration.toMillis() / (double) frames;
    return this;
  }
  
  public AnimationProperties reversed() {
    this.reversed = true;
    return this;
  }
  
  public AnimationProperties setTimingFunction(TimingFunction timingFunction) {
    this.timingFunction = timingFunction;
    return this;
  }
  
  public void reset() {
    this.currentFrame = -1;
    this.animationEnded = false;
  }
  
  public boolean hasNextFrame() {
    return !animationEnded;
  }
  
  public double nextFrame() {
    currentFrame++;
    animationEnded = currentFrame == frames;
    return getValue();
  }
  
  public double getValue() {
    return start + (reversed ? 1 - calculateValue() : calculateValue());
  }
  
  private double calculateValue() {
    return (frames * timingFunction.transform((double) currentFrame / frames)) * step;
  }
  
  public double frameDelay() {
    return frameDelay * currentFrame;
  }
  
  public long frameDelayInTicks() {
    return (long) ((frameDelay / 50) * currentFrame);
  }
  
  public long lastFrameDelayInTicks() {
    return (long) ((frameDelay / 50) * frames);
  }
  
  public void debug() {
    Bukkit.broadcastMessage("frame: " + currentFrame + ", frames: " + frames + ", value: " + getValue() + ", step: " + step + ", delay: " + frameDelay() + "");
  }
}
