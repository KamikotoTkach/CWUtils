package ru.cwcode.cwutils.animation.timingFunction.cubicBezier;

public class CubicBezierInst {
  
  private static final Point P1 = new Point(0, 0);
  private static final Point P4 = new Point(1, 1);
  
  private final Point p2;
  private final Point p3;
  
  public CubicBezierInst(Point p2, Point p3) {
    this.p2 = p2;
    this.p3 = p3;
  }
  
  public double getValue(double t) {
    double dt = 1d - t;
    double dt2 = dt * dt;
    double t2 = t * t;
    
    Point temp = p2.copy();
    
    return P1.copy()
             .scale(dt2 * dt)
             .add(temp.scale(3 * dt2 * t))
             .add(temp.set(p3).scale(3 * dt * t2))
             .add(temp.set(P4).scale(t2 * t))
             .getY();
  }
  
  public Point getPoint(double t) {
    double dt = 1d - t;
    double dt2 = dt * dt;
    double t2 = t * t;
    
    Point temp = p2.copy();
    return P1.copy()
             .scale(dt2 * dt)
             .add(temp.scale(3 * dt2 * t))
             .add(temp.set(p3).scale(3 * dt * t2))
             .add(temp.set(P4).scale(t2 * t));
  }
}