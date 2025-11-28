package ru.cwcode.cwutils.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedRegistrable;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Rotatable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import ru.cwcode.cwutils.numbers.NumbersUtils;
import ru.cwcode.cwutils.player.PlayerUtils;
import ru.cwcode.cwutils.server.PaperServerUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class Packet {
  public static Object GAME_STATE_CHANGE_GAMEMODE;
  
  static {
    try {
      Field[] fields = PacketType.Play.Server.GAME_STATE_CHANGE.getPacketClass().getDeclaredFields();
      if (fields[0].getType() != fields[1].getType())
        GAME_STATE_CHANGE_GAMEMODE = fields[4].get(null);
      else
        GAME_STATE_CHANGE_GAMEMODE = fields[3].get(null);
    } catch (IllegalAccessException e) {
      System.err.println("Failed to load GAME_STATE_CHANGE storage field.");
      e.printStackTrace();
    }
  }
  
  public static void setSlot(Player player, int slot, ItemStack item) {
    setSlot(player, slot, item, 0);
  }
  
  public static void setSlot(Player player, int slot, ItemStack item, int windowID) {
    PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.SET_SLOT);
    
    packet.getIntegers().write(0, windowID);
    
    if (PaperServerUtils.isVersionGreater_1_16_5()) {
      packet.getIntegers().write(1, 0); //state id
      packet.getIntegers().write(2, slot);
    } else {
      packet.getIntegers().write(1, slot);
    }
    
    packet.getItemModifier().write(0, item);
    ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
  }
  
  public static void updateSlot(Player player, int slot) {
    updateSlot(player, slot, 0);
  }
  
  public static void updateSlot(Player player, int slot, int windowID) {
    setSlot(player, slot, player.getInventory().getItem(slot), windowID);
  }
  
  public static void clearInventory(Player player) {
    clearInventory(player, 0);
  }
  
  public static void clearInventory(Player player, int windowID) {
    Inventory inventory = player.getInventory();
    ItemStack air = new ItemStack(Material.AIR);
    
    for (int slot = 0; slot < 36; slot++) {
      ItemStack item = inventory.getItem(slot);
      if (item != null && item.getType().isItem()) {
        Packet.setSlot(player, slot, air, windowID);
      }
    }
  }
  
  public static void spectate(Player player, int entityId) {
    PacketContainer packet = new PacketContainer(PacketType.Play.Server.CAMERA);
    packet.getIntegers().write(0, entityId);
    ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
  }
  
  public static void sendGameModePacket(Player player, GameMode gameMode) {
    PacketContainer packet = new PacketContainer(PacketType.Play.Server.GAME_STATE_CHANGE);
    packet.getModifier().write(0, GAME_STATE_CHANGE_GAMEMODE);
    packet.getFloat().write(0, (float) gameMode.getValue());
    
    ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
  }
  
  public static void spawnLivingEntity(Player player, int id, int entityId, Location loc) {
    PacketContainer packet = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY_LIVING);
    
    packet.getIntegers().write(0, id);
    packet.getIntegers().write(1, entityId);
    packet.getIntegers().write(2, 0);
    packet.getUUIDs().write(0, UUID.randomUUID());
    
    packet.getDoubles().write(0, loc.getX());
    packet.getDoubles().write(1, loc.getY());
    packet.getDoubles().write(2, loc.getZ());
    
    ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
  }
  
  public static void destroyEntity(Player player, int id) {
    PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
    packet.getIntegerArrays().writeSafely(0, new int[]{id});
    packet.getIntLists().writeSafely(0, List.of(id));
    ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
  }
  
  public static void setEntityStatus(Player receiver, Entity entity, byte status) {
    PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_METADATA);
    packet.getIntegers().write(0, entity.getEntityId()); //Set packet's entity id
    WrappedDataWatcher watcher = new WrappedDataWatcher(); //Create data watcher, the Entity Metadata packet requires this
    WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class); //Found this through google, needed for some stupid reason
    watcher.setEntity(entity); //Set the new data watcher's target
    watcher.setObject(0, serializer, status); //Set status to glowing, found on protocol page
    packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects()); //Make the packet's datawatcher the one we created
    ProtocolLibrary.getProtocolManager().sendServerPacket(receiver, packet);
  }
  
  public static void setEntityFrozen(Player receiver, Entity entity, int ticksFrozen) {
    PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_METADATA);
    packet.getIntegers().write(0, entity.getEntityId()); //Set packet's entity id
    WrappedDataWatcher watcher = new WrappedDataWatcher(); //Create data watcher, the Entity Metadata packet requires this
    WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Integer.class); //Found this through google, needed for some stupid reason
    watcher.setEntity(entity); //Set the new data watcher's target
    watcher.setObject(7, serializer, ticksFrozen); //Set status to glowing, found on protocol page
    packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects()); //Make the packet's datawatcher the one we created
    ProtocolLibrary.getProtocolManager().sendServerPacket(receiver, packet);
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
    manager.sendServerPacket(receiver, packet);
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
    manager.sendServerPacket(receiver, packet);
  }
  
  public static void setHead(Player receiver, PlayerProfile playerProfile, Location location, BlockFace rotation) {
    receiver.sendBlockChange(location, Material.PLAYER_HEAD.createBlockData((blockData -> ((Rotatable) blockData).setRotation(rotation))));
    Packet.updateHead(receiver, playerProfile, location);
  }
  
  public static void updateHead(Player receiver, PlayerProfile playerProfile, Location location) {
    if (playerProfile == null) return;
    ProtocolManager manager = ProtocolLibrary.getProtocolManager();
    PacketContainer packet = manager.createPacket(PacketType.Play.Server.TILE_ENTITY_DATA);
    
    packet.getBlockPositionModifier().write(0, new BlockPosition(
      location.getBlockX(),
      location.getBlockY(),
      location.getBlockZ())
    );
    
    if (PaperServerUtils.isVersionGreater("1.17.1")) {
      packet.getBlockEntityTypeModifier().write(0, WrappedRegistrable.blockEntityType("skull"));
    } else {
      packet.getIntegers().write(0, 4);
    }
    
    NbtCompound base = NbtFactory.ofCompound("");
    base.put("x", location.getBlockX());
    base.put("y", location.getBlockY());
    base.put("z", location.getBlockZ());
    base.put("id", "minecraft:skull");
    
    NbtCompound nbt = NbtFactory.ofCompound("SkullOwner");
    
    nbt.put("Id", NumbersUtils.convertToInts(playerProfile.getId()));
    nbt.put("Name", "");
    
    NbtCompound properties = NbtFactory.ofCompound("Properties");
    NbtCompound skin = NbtFactory.ofCompound("");
    
    skin.put("Value", PlayerUtils.getTextureValue(playerProfile));
    properties.put("textures", NbtFactory.ofList("textures", skin));
    
    nbt.put("Properties", properties);
    
    base.put("SkullOwner", nbt);
    
    packet.getNbtModifier().write(0, base);
    
    try {
      manager.sendServerPacket(receiver, packet);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public static void blockUpdate(Collection<? extends Player> players, Location location, BlockData blockData) {
    ProtocolManager manager = ProtocolLibrary.getProtocolManager();
    
    PacketContainer blockUpdatePacket = getBlockUpdatePacket(location, blockData);
    
    manager.broadcastServerPacket(blockUpdatePacket, players);
  }
  
  public static PacketContainer getBlockUpdatePacket(Location location, BlockData blockData) {
    ProtocolManager manager = ProtocolLibrary.getProtocolManager();
    PacketContainer packet = manager.createPacket(PacketType.Play.Server.BLOCK_CHANGE);
    
    packet.getBlockPositionModifier().write(0, new BlockPosition(location.toVector()));
    packet.getBlockData().write(0, WrappedBlockData.createData(blockData));
    
    return packet;
  }
}
