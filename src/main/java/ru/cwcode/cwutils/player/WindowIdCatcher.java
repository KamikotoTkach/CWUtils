package ru.cwcode.cwutils.player;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerOpenWindow;
import org.bukkit.entity.Player;

import java.util.WeakHashMap;

public class WindowIdCatcher {
  static WeakHashMap<Player, Integer> windowIDs = new WeakHashMap<>();
  static boolean loaded = false;

  public static void load() {
    if (loaded) return;
    loaded = true;

    PacketEvents.getAPI().getEventManager().registerListener(new PacketListenerAbstract(PacketListenerPriority.NORMAL) {
      @Override
      public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() != PacketType.Play.Server.OPEN_WINDOW) return;

        Player player = event.getPlayer();
        windowIDs.put(player, new WrapperPlayServerOpenWindow(event).getContainerId());
      }

      @Override
      public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.CLOSE_WINDOW) return;

        Player player = event.getPlayer();
        windowIDs.put(player, 0);
      }
    });
  }

  public static int getWindowID(Player player) {
    return windowIDs.getOrDefault(player, 0);
  }
}
