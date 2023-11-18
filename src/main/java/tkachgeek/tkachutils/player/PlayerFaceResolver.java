package tkachgeek.tkachutils.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.plugin.java.JavaPlugin;
import tkachgeek.tkachutils.files.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PlayerFaceResolver {
  private final JavaPlugin plugin;
  private String filler = "⬛";
  
  public PlayerFaceResolver(JavaPlugin plugin) {
    this.plugin = plugin;
  }
  
  public void setFiller(String filler) {
    this.filler = filler;
  }
  
  public List<Component> getHeadLines(String name) {
    var list = new ArrayList<Component>();
    var img = getPlayerFace(name);
    for (int h = 0; h < img.getHeight(); h++) {
      var text = Component.empty();
      for (int w = 0; w < img.getWidth(); w++) {
        text = text.append(Component.text(filler, TextColor.color(img.getRGB(w, h))));
      }
      list.add(text);
    }
    return list;
  }
  
  public BufferedImage getPlayerFace(String name) {
    File file = resolveFile(name);
    try {
      if (file.exists()) {
        return ImageIO.read(file);
      } else {
        downloadHead(name);
      }
      return ImageIO.read(file);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  public void downloadHead(String name) throws IOException {
    FileUtils.downloadFileTo("https://minotar.net/helm/" + name + "/8", resolvePath(name));
  }
  
  public CompletableFuture<List<Component>> getHeadLinesCompletable(String name) {
    return CompletableFuture.supplyAsync(() -> getHeadLines(name));
  }
  
  public CompletableFuture<BufferedImage> getPlayerFaceCompletable(String name) {
    return CompletableFuture.supplyAsync(() -> getPlayerFace(name));
  }
  
  String resolvePath(String name) {
    return plugin.getDataFolder()
                 .toPath()
                 .resolve("heads")
                 .resolve(name + ".png")
                 .toAbsolutePath()
                 .toString();
  }
  
  File resolveFile(String name) {
    Path path = plugin.getDataFolder()
                      .toPath()
                      .resolve("heads")
                      .resolve(name + ".png");
    
    if (!Files.exists(path)) {
      try {
        com.google.common.io.Files.createParentDirs(path.toFile());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return path.toFile();
  }
}
