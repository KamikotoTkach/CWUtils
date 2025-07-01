package ru.cwcode.cwutils.worldEdit;

import com.google.common.io.Files;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.*;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.math.transform.Transform;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.util.Direction;
import com.sk89q.worldedit.world.block.BlockType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class WorldEditUtils {
  public static final List<Direction> CARDINAL_DIRECTIONS = Arrays.asList(Direction.EAST, Direction.NORTH, Direction.SOUTH, Direction.WEST);
  static Logger log = Logger.getLogger("WorldEditUtils");
  
  public static BlockVector3 pasteClipboard(Location loc, Clipboard clipboard, boolean ignoreAirBlocks,
                                            Consumer<EditSession> customizeEditSession,
                                            Consumer<ClipboardHolder> customizeClipboardHolder,
                                            Set<Material> iterateMaterials,
                                            Consumer<Block> blockIterate) {
    
    try (EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(loc.getWorld()))) {
      ClipboardHolder holder = new ClipboardHolder(clipboard);
      
      if (customizeEditSession != null) customizeEditSession.accept(editSession);
      if (customizeClipboardHolder != null) customizeClipboardHolder.accept(holder);
      
      Operation operation = holder.createPaste(editSession)
                                  .ignoreAirBlocks(ignoreAirBlocks)
                                  .to(BukkitAdapter.adapt(loc).toVector().toBlockPoint())
                                  .build();
      
      Operations.complete(operation);
      
      editSession.close();
      
      if (blockIterate != null) {
        BlockVector3 origin = clipboard.getOrigin();
        Transform transform = holder.getTransform();
        
        for (BlockVector3 blockVector3 : clipboard.getRegion()) {
          BlockType material = clipboard.getBlock(blockVector3).getBlockType();
          Material bukkitMaterial = BukkitAdapter.adapt(material);
          
          if (ignoreAirBlocks && material.getMaterial().isAir()
              || iterateMaterials != null && !iterateMaterials.contains(bukkitMaterial)) {
            continue;
          }
          
          Vector3 relativePosition = blockVector3.subtract(origin).toVector3();
          Vector3 transformedRelative = transform.apply(relativePosition);
          
          Location worldLocation = loc.clone().add(transformedRelative.getX(),
                                                   transformedRelative.getY(),
                                                   transformedRelative.getZ());
          
          blockIterate.accept(worldLocation.getBlock());
        }
      }
      
      return clipboard.getDimensions();
    } catch (WorldEditException e) {
      e.printStackTrace();
    }
    
    return null;
  }
  
  public static BlockVector3 pasteClipboard(Location loc, Clipboard clipboard, boolean ignoreAirBlocks) {
    return pasteClipboard(loc, clipboard, ignoreAirBlocks, null, null, null, null);
  }
  
  public static Clipboard saveSelection(Player player, Path path) {
    try {
      WorldEditPlugin worldEditPlugin = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
      Region selection = worldEditPlugin.getSession(player).getSelection(BukkitAdapter.adapt(player.getWorld()));
      
      return saveRegion(player.getWorld(), selection, path);
    } catch (WorldEditException e) {
      throw new RuntimeException(e);
    }
  }
  
  public static Clipboard saveRegion(World world, Region region, Path path) {
    
    try {
      BlockArrayClipboard clipboard = new BlockArrayClipboard(region);
      
      clipboard.setOrigin(BlockVector3.at(clipboard.getMinimumPoint().getX(), clipboard.getMinimumPoint().getY(), clipboard.getMinimumPoint().getZ()));
      
      try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(BukkitAdapter.adapt(world), -1)) {
        ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(
          editSession, region, clipboard, region.getMinimumPoint()
        );
        Operations.complete(forwardExtentCopy);
      } catch (WorldEditException e) {
        e.printStackTrace();
      }
      
      File file = path.toFile();
      Files.createParentDirs(file);
      
      try (ClipboardWriter writer = BuiltInClipboardFormat.SPONGE_SCHEMATIC.getWriter(new FileOutputStream(file))) {
        writer.write(clipboard);
      } catch (IOException e) {
        e.printStackTrace();
        return null;
      }
      
      return clipboard;
    } catch (Exception ex) {
      return null;
    }
  }
  
  public static Clipboard loadSchematic(Path path) {
    Clipboard clipboard;
    
    File file = path.toFile();
    
    ClipboardFormat format = ClipboardFormats.findByFile(file);
    
    try {
      try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
        clipboard = reader.read();
      }
    } catch (Exception e) {
      log.warning("Не получается прочесть схематику " + path + ": " + e.getMessage());
      return null;
    }
    
    clipboard.setOrigin(clipboard.getMinimumPoint());
    
    return clipboard;
  }
  
  public static AffineTransform cardinalDirectionToTransform(Direction direction) {
    if (direction == null) return null;
    
    AffineTransform transform = new AffineTransform();
    
    switch (direction) {
      case NORTH:
        break;
      case EAST:
        transform = transform.rotateY(90);
        break;
      case SOUTH:
        transform = transform.rotateY(180);
        break;
      case WEST:
        transform = transform.rotateY(270);
        break;
      default:
        throw new IllegalArgumentException("Direction %s is not cardinal".formatted(direction.name()));
    }
    
    return transform;
  }
}
