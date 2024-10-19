package ru.cwcode.cwutils.bounceable;

import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.entity.EntityExplodeEvent;

public interface BounceableTNTPrimed extends Bounceable<TNTPrimed, EntityExplodeEvent> {
  @Override
  default Class<TNTPrimed> getEntityClass() {
    return TNTPrimed.class;
  }
  
  @Override
  default void onExecute(EntityExplodeEvent event) {
  }
}
