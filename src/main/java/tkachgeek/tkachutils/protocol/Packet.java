package tkachgeek.tkachutils.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class Packet {
  public static void setSlot(Player player, int slot, ItemStack item) {
    try {
      PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.SET_SLOT);
      packet.getIntegers().write(0, 0);
      packet.getIntegers().write(1, slot);
      packet.getItemModifier().write(0, item);
      ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
    } catch (InvocationTargetException ignored) {
    
    }
  }
  
  public static void updateSlot(Player player, int slot) {
    setSlot(player, slot, player.getInventory().getItem(slot));
  }
  
  public static void spawnLivingEntity(Player player, int id, int entityId, Location loc) {
    try {
      PacketContainer packet = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY_LIVING);
      
      packet.getIntegers().write(0, id);
      packet.getIntegers().write(1, entityId);
      packet.getIntegers().write(2, 0);
      packet.getUUIDs().write(0, UUID.randomUUID());
      
      packet.getDoubles().write(0, loc.getX());
      packet.getDoubles().write(1, loc.getY());
      packet.getDoubles().write(2, loc.getZ());
      
      ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
    } catch (InvocationTargetException ignored) {
    
    }
  }
  
  public static void destroyEntity(Player player, int id) {
    try {
      PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
      packet.getIntegerArrays().write(0, new int[]{id});
      ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
    } catch (InvocationTargetException ignored) {
    
    }
  }
}
