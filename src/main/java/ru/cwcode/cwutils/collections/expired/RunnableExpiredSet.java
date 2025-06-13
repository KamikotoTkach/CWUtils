package ru.cwcode.cwutils.collections.expired;

import ru.cwcode.cwutils.collections.ExpiredSet;

import java.time.Duration;

public abstract class RunnableExpiredSet<Element> extends ExpiredSet<Element> {
  protected abstract void register(Element element, Runnable runnable, Duration duration);
  
  public void setExpired(Element element, Runnable runnable, Duration duration) {
    this.setExpired(element, duration);
    this.register(element, runnable, duration);
  }
  
  protected Runnable getAction(Element element, Runnable runnable) {
    return () -> {
      if (!this.remove(element)) return;
      runnable.run();
    };
  }
}
