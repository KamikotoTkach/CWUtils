package ru.cwcode.cwutils.animation.timingFunction.cubicBezier;

public class Point {
  private double x;
  private double y;
  
  public Point(Point point) {
    this.x = point.x;
    this.y = point.y;
  }
  
  public Point(double x, double y) {
    this.x = x;
    this.y = y;
  }
  
  public Point copy() {
    return new Point(this);
  }
  
  public Point set(Point point) {
    this.x = point.x;
    this.y = point.y;
    return this;
  }
  
  public Point add(double scalar) {
    this.x += scalar;
    this.y += scalar;
    return this;
  }
  
  public Point add(double x, double y) {
    this.x += x;
    this.y += y;
    return this;
  }
  
  public Point add(Point point) {
    return add(point.x, point.y);
  }
  
  public Point scale(double scalar) {
    this.x *= scalar;
    this.y *= scalar;
    return this;
  }
  
  public Point scale(double x, double y) {
    this.x *= x;
    this.y *= y;
    return this;
  }
  
  public Point scale(Point point) {
    return scale(point.x, point.y);
  }
  
  public double getY() {
    return y;
  }
  
  public double getX() {
    return x;
  }
}
