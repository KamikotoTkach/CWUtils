package ru.cwcode.cwutils.logger;

import org.bukkit.plugin.java.JavaPlugin;

public class PaperLogger implements Logger {
  private final java.util.logging.Logger logger;
  
  public PaperLogger(JavaPlugin plugin) {
    this.logger = plugin.getLogger();
  }
  
  @Override
  public void info(String message) {
    this.logger.info(message);
  }
  
  @Override
  public void warn(String message) {
    this.logger.warning(message);
  }
  
  @Override
  public void error(String message) {
    this.warn(message);
  }
}
