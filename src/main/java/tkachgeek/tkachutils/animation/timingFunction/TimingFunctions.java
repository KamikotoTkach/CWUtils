package tkachgeek.tkachutils.animation.timingFunction;

public class TimingFunctions {
  public static TimingFunction ease = new CubicBezier(0.25, 0.1, 0.25, 1.0);
  public static TimingFunction linear = new Linear();
  public static TimingFunction easeIn = new CubicBezier(0.42, 0, 1.0, 1.0);
  public static TimingFunction easeOut = new CubicBezier(0, 0, 0.58, 1.0);
  public static TimingFunction easeInOut = new CubicBezier(0.42, 0, 0.58, 1.0);
  
  public static CubicBezier ofCubicBezier(double p0, double p1, double p2, double p3) {
    return new CubicBezier(p0, p1, p2, p3);
  }
}
