package ru.cwcode.cwutils.logger;

public class VelocityLogger implements Logger {
  private final org.slf4j.Logger logger;
  
  public VelocityLogger(org.slf4j.Logger logger) {
    this.logger = logger;
  }
  
  @Override
  public void info(String message) {
    logger.info(message);
  }
  
  @Override
  public void warn(String message) {
    logger.warn(message);
  }
  
  @Override
  public void error(String message) {
    logger.error(message);
  }
}
