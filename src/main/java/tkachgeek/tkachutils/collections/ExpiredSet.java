package tkachgeek.tkachutils.collections;

import tkachgeek.tkachutils.datetime.Expireable;

import java.time.Duration;
import java.util.HashMap;
import java.util.Set;

public class ExpiredSet<Element> {
   protected final HashMap<Element, Expireable> expired = new HashMap<>();

   public void setExpired(Element element, Duration duration) {
      this.expired.put(element, new Expireable(duration.toMillis(), System.currentTimeMillis()));
   }

   public boolean isActive(Element element) {
      return this.getStatus(element).equals(Status.ACTIVE);
   }

   public Status getStatus(Element element) {
      Expireable expireable = this.expired.get(element);

      if (expireable == null) {
         return Status.NO_ELEMENT;
      }

      return expireable.isExpired() ? Status.ACTIVE : Status.EXPIRED;
   }

   public double getPercent(Element element) {
      Expireable expireable = this.expired.get(element);

      if (expireable == null) return -1;

      return expireable.getPercent();
   }

   public boolean has(Element expired) {
      return this.expired.containsKey(expired);
   }

   public Set<Element> getExpired() {
      return this.expired.keySet();
   }

   public HashMap<Element, Expireable> entries() {
      return this.expired;
   }

   public enum Status {
      ACTIVE,
      EXPIRED,
      NO_ELEMENT
   }
}
