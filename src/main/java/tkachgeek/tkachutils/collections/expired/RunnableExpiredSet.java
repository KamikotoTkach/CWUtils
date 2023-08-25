package tkachgeek.tkachutils.collections.expired;

import tkachgeek.tkachutils.collections.ExpiredSet;

import java.time.Duration;

public abstract class RunnableExpiredSet<Element> extends ExpiredSet<Element> {
   public void setExpired(Element element, Runnable runnable, Duration duration) {
      this.setExpired(element, duration);
      this.register(element, runnable, duration);
   }

   public boolean remove(Element element) {
      return this.expired.remove(element) != null;
   }

   protected Runnable getAction(Element element, Runnable runnable) {
      return () -> {
         if (!this.remove(element)) return;
         runnable.run();
      };
   }

   protected abstract void register(Element element, Runnable runnable, Duration duration);
}
