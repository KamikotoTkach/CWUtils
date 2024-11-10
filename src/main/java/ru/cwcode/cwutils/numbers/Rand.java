package ru.cwcode.cwutils.numbers;

import java.util.concurrent.ThreadLocalRandom;

public class Rand {
  public static int ofInt(int max) {
    return ofInt(0, max);
  }
  
  public static int ofInt(int min, int max) {
    return ThreadLocalRandom.current().nextInt(min, max);
  }
  
  public static long ofLong(long max) {
    return ofLong(0, max);
  }
  
  public static long ofLong(long min, long max) {
    return ThreadLocalRandom.current().nextLong(min, max);
  }
  
  public static float ofFloat(float max) {
    return ofFloat(0.0f, max);
  }
  
  public static float ofFloat(float min, float max) {
    return ThreadLocalRandom.current().nextFloat(min, max);
  }
  
  public static double ofDouble(double max) {
    return ofDouble(0.0, max);
  }
  
  public static double ofDouble(double min, double max) {
    return ThreadLocalRandom.current().nextDouble(min, max);
  }
  
  public static boolean bool() {
    return ThreadLocalRandom.current().nextBoolean();
  }
  
  public static boolean testChance(double chance) {
    if (chance >= 1) return true;
    if (chance <= 0) return false;
    return ThreadLocalRandom.current().nextDouble() < chance;
  }
}
