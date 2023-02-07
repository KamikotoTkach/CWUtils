package tkachgeek.tkachutils.animation.timingFunction;

public class Linear implements TimingFunction {
  @Override
  public double transform(double value) {
    return value;
  }
}
