package ru.cwcode.cwutils.numbers;

import java.util.concurrent.ThreadLocalRandom;

public class Rand {
  public static int ofInt(int max) {
    return ofInt(0, max);
  }
  
  public static int ofInt(int min, int max) {
    return ThreadLocalRandom.current().nextInt(min, max);
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
