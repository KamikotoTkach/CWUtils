package tkachgeek.tkachutils.player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.entity.Player;

import java.util.WeakHashMap;

public class WindowIdCatcher {
  static WeakHashMap<Player, Integer> windowIDs = new WeakHashMap<>();
  static boolean loaded = false;
  
  public static void load() {
    if (loaded) return;
    loaded = true;
    
    ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(ProtocolLibrary.getPlugin(), PacketType.Play.Server.OPEN_WINDOW) {
      @Override
      public void onPacketSending(PacketEvent event) {
        windowIDs.put(event.getPlayer(), event.getPacket().getIntegers().read(0));
      }
    });
    
    ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(ProtocolLibrary.getPlugin(), PacketType.Play.Client.CLOSE_WINDOW) {
      @Override
      public void onPacketReceiving(PacketEvent event) {
        windowIDs.put(event.getPlayer(), 0);
      }
    });
  }
  
  public static int getWindowID(Player player) {
    return windowIDs.getOrDefault(player, 0);
  }
}
