package ru.cwcode.cwutils.protocol;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.nbt.NBTIntArray;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.world.blockentity.BlockEntityTypes;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockChange;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockEntityData;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerCamera;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChangeGameState;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityRelativeMove;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityTeleport;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetSlot;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnLivingEntity;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
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

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class Packet {

  public static void setSlot(Player player, int slot, ItemStack item) {
    setSlot(player, slot, item, 0);
  }

  public static void setSlot(Player player, int slot, ItemStack item, int windowID) {
    // packetevents writes the state id only on versions that use it (1.17.1+) and normalizes the slot layout
    send(player, new WrapperPlayServerSetSlot(windowID, 0, slot, toPacketItem(item)));
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
    send(player, new WrapperPlayServerCamera(entityId));
  }

  public static void sendGameModePacket(Player player, GameMode gameMode) {
    send(player, new WrapperPlayServerChangeGameState(WrapperPlayServerChangeGameState.Reason.CHANGE_GAME_MODE, gameMode.getValue()));
  }

  public static void spawnLivingEntity(Player player, int id, int entityId, Location loc) {
    EntityType type = EntityTypes.getById(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion(), entityId);

    send(player, new WrapperPlayServerSpawnLivingEntity(
      id, UUID.randomUUID(), type,
      new Vector3d(loc.getX(), loc.getY(), loc.getZ()),
      0f, 0f, 0f,
      new Vector3d(0, 0, 0),
      List.of()));
  }

  public static void destroyEntity(Player player, int id) {
    send(player, new WrapperPlayServerDestroyEntities(id));
  }

  public static void setEntityStatus(Player receiver, Entity entity, byte status) {
    List<EntityData<?>> metadata = List.of(new EntityData<>(0, EntityDataTypes.BYTE, status));
    send(receiver, new WrapperPlayServerEntityMetadata(entity.getEntityId(), metadata));
  }

  public static void setEntityFrozen(Player receiver, Entity entity, int ticksFrozen) {
    List<EntityData<?>> metadata = List.of(new EntityData<>(7, EntityDataTypes.INT, ticksFrozen));
    send(receiver, new WrapperPlayServerEntityMetadata(entity.getEntityId(), metadata));
  }

  public static void teleportEntity(Player receiver, int entityId, Location location) {
    send(receiver, new WrapperPlayServerEntityTeleport(entityId,
      new Vector3d(location.getX(), location.getY(), location.getZ()), 0f, 0f, false));
  }

  public static void moveEntity(Player receiver, int entityId, Vector vector) {
    // packetevents accepts the delta in blocks and encodes it into the 1/4096 units on the wire
    send(receiver, new WrapperPlayServerEntityRelativeMove(entityId, vector.getX(), vector.getY(), vector.getZ(), false));
  }

  public static void setHead(Player receiver, PlayerProfile playerProfile, Location location, BlockFace rotation) {
    receiver.sendBlockChange(location, Material.PLAYER_HEAD.createBlockData((blockData -> ((Rotatable) blockData).setRotation(rotation))));
    Packet.updateHead(receiver, playerProfile, location);
  }

  public static void updateHead(Player receiver, PlayerProfile playerProfile, Location location) {
    if (playerProfile == null) return;

    NBTCompound base = new NBTCompound();
    base.setTag("x", new NBTInt(location.getBlockX()));
    base.setTag("y", new NBTInt(location.getBlockY()));
    base.setTag("z", new NBTInt(location.getBlockZ()));
    base.setTag("id", new NBTString("minecraft:skull"));

    NBTCompound skullOwner = new NBTCompound();
    skullOwner.setTag("Id", new NBTIntArray(NumbersUtils.convertToInts(playerProfile.getId())));
    skullOwner.setTag("Name", new NBTString(""));

    NBTCompound skin = new NBTCompound();
    skin.setTag("Value", new NBTString(PlayerUtils.getTextureValue(playerProfile)));

    NBTList<NBTCompound> textures = NBTList.createCompoundList();
    textures.addTag(skin);

    NBTCompound properties = new NBTCompound();
    properties.setTag("textures", textures);

    skullOwner.setTag("Properties", properties);
    base.setTag("SkullOwner", skullOwner);

    Vector3i position = new Vector3i(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    send(receiver, new WrapperPlayServerBlockEntityData(position, BlockEntityTypes.SKULL, base));
  }

  public static void blockUpdate(Collection<? extends Player> players, Location location, BlockData blockData) {
    WrapperPlayServerBlockChange packet = getBlockUpdatePacket(location, blockData);

    for (Player player : players) {
      send(player, packet);
    }
  }

  public static WrapperPlayServerBlockChange getBlockUpdatePacket(Location location, BlockData blockData) {
    Vector3i position = new Vector3i(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    return new WrapperPlayServerBlockChange(position, SpigotConversionUtil.fromBukkitBlockData(blockData));
  }

  private static com.github.retrooper.packetevents.protocol.item.ItemStack toPacketItem(ItemStack item) {
    return item == null
           ? com.github.retrooper.packetevents.protocol.item.ItemStack.EMPTY
           : SpigotConversionUtil.fromBukkitItemStack(item);
  }

  private static void send(Player player, PacketWrapper<?> packet) {
    PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
  }
}
