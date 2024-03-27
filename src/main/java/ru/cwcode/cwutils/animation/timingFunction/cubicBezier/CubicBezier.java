package ru.cwcode.cwutils.animation.timingFunction.cubicBezier;

import ru.cwcode.cwutils.animation.timingFunction.TimingFunction;

public class CubicBezier implements TimingFunction {
  CubicBezierInst inst;
  
  public CubicBezier(double p0, double p1, double p2, double p3) {
    inst = new CubicBezierInst(new Point(p0, p1), new Point(p2, p3));
  }
  
  @Override
  public double transform(double value) {
    return inst.getValue(value);
  }
}
