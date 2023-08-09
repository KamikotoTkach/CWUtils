package tkachgeek.tkachutils.scheduler;

import org.bukkit.plugin.java.JavaPlugin;

public class AbstractScheduler {
  protected static int increment = 0;
  protected final int id = increment++;
  protected int taskId;
  protected JavaPlugin registrant = null;
  protected volatile boolean asyncTask = false;
  protected volatile boolean infinite = false;
  protected volatile boolean blocked = false;
  protected volatile boolean running = false;
}
