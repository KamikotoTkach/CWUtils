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
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

public class WorldEditUtils {
  static Logger log = Logger.getLogger("WorldEditUtils");
  
  public static BlockVector3 pasteClipboard(Location loc, Clipboard clipboard, boolean ignoreAirBlocks) {
    
    try (EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(loc.getWorld()))) {
      ClipboardHolder holder = new ClipboardHolder(clipboard);
      
      Operation operation = holder.createPaste(editSession)
                                  .ignoreAirBlocks(ignoreAirBlocks)
                                  .to(BukkitAdapter.adapt(loc).toVector().toBlockPoint())
                                  .build();
      
      Operations.complete(operation);
    } catch (WorldEditException e) {
      e.printStackTrace();
      return null;
    }
    
    return clipboard.getDimensions();
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
      log.warning("Не получается прочесть схематику " + path);
      return null;
    }
    
    clipboard.setOrigin(clipboard.getMinimumPoint());
    
    return clipboard;
  }
}
