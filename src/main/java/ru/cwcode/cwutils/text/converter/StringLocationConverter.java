package ru.cwcode.cwutils.text.converter;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import ru.cwcode.cwutils.numbers.NumbersUtils;

public class StringLocationConverter implements StringObjectConverter<Location> {
  @Override
  public String convertToString(Location location) {
    return "%s, %s %s %s, %s %s".formatted(location.getWorld().getName(),
                                           location.getX(),
                                           location.getY(),
                                           location.getZ(),
                                           location.getPitch(),
                                           location.getYaw());
  }
  
  @Override
  public String convertToStringFancy(Location location) {
    return "%s, %s %s %s, %s %s".formatted(location.getWorld().getName(),
                                           NumbersUtils.round(location.getX(), 1),
                                           NumbersUtils.round(location.getY(), 1),
                                           NumbersUtils.round(location.getZ(), 1),
                                           NumbersUtils.round(location.getPitch(), 1),
                                           NumbersUtils.round(location.getYaw(), 1));
  }
  
  @Override
  public Location convertFromString(String s) {
    String[] split = s.split(" ");
    
    return new Location(Bukkit.getWorld(split[0]),
                        Double.parseDouble(split[1]),
                        Double.parseDouble(split[2]),
                        Double.parseDouble(split[3]),
                        Float.parseFloat(split[4]),
                        Float.parseFloat(split[5]));
  }
}
