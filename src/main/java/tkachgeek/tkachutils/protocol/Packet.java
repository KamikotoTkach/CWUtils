package tkachgeek.tkachutils.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
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
  
  public static void setEntityStatus(Player receiver, Entity entity, byte status) {
    PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_METADATA);
    packet.getIntegers().write(0, entity.getEntityId()); //Set packet's entity id
    WrappedDataWatcher watcher = new WrappedDataWatcher(); //Create data watcher, the Entity Metadata packet requires this
    WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class); //Found this through google, needed for some stupid reason
    watcher.setEntity(entity); //Set the new data watcher's target
    watcher.setObject(0, serializer, (byte) (0x40)); //Set status to glowing, found on protocol page
    packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects()); //Make the packet's datawatcher the one we created
    try {
      ProtocolLibrary.getProtocolManager().sendServerPacket(receiver, packet);
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
  }
  
  public static void setEntityFrozen(Player receiver, Entity entity, int ticksFrozen) {
    PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_METADATA);
    packet.getIntegers().write(0, entity.getEntityId()); //Set packet's entity id
    WrappedDataWatcher watcher = new WrappedDataWatcher(); //Create data watcher, the Entity Metadata packet requires this
    WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Integer.class); //Found this through google, needed for some stupid reason
    watcher.setEntity(entity); //Set the new data watcher's target
    watcher.setObject(7, serializer, ticksFrozen); //Set status to glowing, found on protocol page
    packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects()); //Make the packet's datawatcher the one we created
    try {
      ProtocolLibrary.getProtocolManager().sendServerPacket(receiver, packet);
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
  }
}
