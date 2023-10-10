package tkachgeek.tkachutils.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import com.comphenix.protocol.wrappers.nbt.NbtType;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

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

  public static void teleportEntity(Player receiver, int entityId, Location location) {
    // Создать новый пакет телепортации сущности
    ProtocolManager manager = ProtocolLibrary.getProtocolManager();
    PacketContainer packet = manager.createPacket(PacketType.Play.Server.ENTITY_TELEPORT);

    // Установить ID сущности в пакете
    packet.getIntegers().write(0, entityId);

    // Установить новые координаты в пакете
    packet.getDoubles()
          .write(0, location.getX())
          .write(1, location.getY())
          .write(2, location.getZ());

    // Отправить пакет игроку
    try {
      manager.sendServerPacket(receiver, packet);
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  public static void moveEntity(Player receiver, int entityId, Vector vector) {
    // Создать новый пакет телепортации сущности
    ProtocolManager manager = ProtocolLibrary.getProtocolManager();
    PacketContainer packet = manager.createPacket(PacketType.Play.Server.REL_ENTITY_MOVE);

    // Установить ID сущности в пакете
    packet.getIntegers().write(0, entityId);

    // Устанавливаем смещение по X, Y и Z в единицах 1/4096 блока
    packet.getShorts()
          .write(0, (short) (vector.getX() * 4096))
          .write(1, (short) (vector.getY() * 4096))
          .write(2, (short) (vector.getZ() * 4096));

    // Устанавливаем флаги для поворота головы и тела
    packet.getBooleans().write(0, false);
    packet.getBooleans().write(1, false);

    // Отправить пакет игроку
    try {
      manager.sendServerPacket(receiver, packet);
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  public static void setHead(JavaPlugin plugin, Player receiver, PlayerProfile playerProfile, Location location) {
    receiver.sendBlockChange(location, Material.PLAYER_HEAD.createBlockData());
    Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> Packet.updateHead(receiver, playerProfile, location), 10L);
  }

  public static void updateHead(Player receiver, PlayerProfile playerProfile, Location location) {
    if (playerProfile == null) return;

    // Create a new packet container of type TileEntityData
    ProtocolManager manager = ProtocolLibrary.getProtocolManager();
    PacketContainer packet = manager.createPacket(PacketType.Play.Server.TILE_ENTITY_DATA);
// Set the location of the skull block
    BlockPosition blockPosition = new BlockPosition(location.toVector());
    packet.getBlockPositionModifier().write(0, blockPosition);

    // Set the action to 4 (update skull entity data)
    packet.getIntegers().write(0, 4);

    // Create a new NBT compound tag for the skull data
    NbtCompound skullData = NbtFactory.ofCompound("");

    // Set the SkullType to 3 (player head)
    skullData.put("SkullType", (byte) 3);

    // Set the SkullOwner to a compound tag with the profile data
    NbtCompound skullOwner = NbtFactory.ofCompound("");
    skullOwner.put("Name", NbtFactory.ofWrapper(NbtType.TAG_STRING, "SKULL"));
    skullOwner.put("Id", NbtFactory.ofWrapper(NbtType.TAG_INT_ARRAY, playerProfile.getId().toString()));
    NbtCompound properties = NbtFactory.ofCompound("");
    for (ProfileProperty property : playerProfile.getProperties()) {
      NbtCompound propertyTag = NbtFactory.ofCompound("");
      propertyTag.put("Value", property.getValue());
      if (property.isSigned()) {
        propertyTag.put("Signature", property.getSignature());
      }
      properties.put(property.getName(), propertyTag);
    }
    skullOwner.put("Properties", properties);
    skullData.put("SkullOwner", skullOwner);

    // Set the packet's NBT compound modifier to the skull data tag
    packet.getNbtModifier().write(0, skullData);

    // Send the packet to the target player
    try {
      manager.sendServerPacket(receiver, packet);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
