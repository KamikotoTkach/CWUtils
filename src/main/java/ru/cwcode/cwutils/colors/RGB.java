package ru.cwcode.cwutils.colors;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.util.RGBLike;

public class RGB implements RGBLike {
  int r = 0;
  int g = 0;
  int b = 0;
  
  public RGB(NamedTextColor color) {
    r = color.red();
    g = color.green();
    b = color.blue();
  }
  
  public RGB() {
  }
  
  public RGB(int r, int g, int b) {
    this.r = r;
    this.g = g;
    this.b = b;
  }
  
  @Override
  public int red() {
    return r;
  }
  
  @Override
  public int green() {
    return g;
  }
  
  @Override
  public int blue() {
    return b;
  }
  
  @Override
  public String toString() {
    return "RGB{" +
       "r=" + r +
       ", g=" + g +
       ", b=" + b +
       '}';
  }
}