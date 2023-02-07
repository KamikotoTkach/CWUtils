package tkachgeek.tkachutils.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.plugin.java.JavaPlugin;
import tkachgeek.tkachutils.files.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PlayerFaceResolver {
  JavaPlugin plugin;
  private String filler = "⬛";
  
  public PlayerFaceResolver(JavaPlugin plugin) {
    this.plugin = plugin;
  }
  
  public void setFiller(String filler) {
    this.filler = filler;
  }
  
  public CompletableFuture<ArrayList<Component>> getHeadLines(String name) {
    var list = new ArrayList<Component>();
    var imageCompletableFuture = getPlayerFace(name);
    return imageCompletableFuture.thenApplyAsync(x -> {
      if (x == null) return new ArrayList<>();
      
      for (int h = 0; h < x.getHeight(); h++) {
        var text = Component.empty();
        for (int w = 0; w < x.getWidth(); w++) {
          text = text.append(Component.text(filler, TextColor.color(x.getRGB(w, h))));
        }
        list.add(text);
      }
      return list;
    });
  }
  
  public CompletableFuture<BufferedImage> getPlayerFace(String name) {
    File file = resolveFile(name);
    var cf = new CompletableFuture<BufferedImage>();
    
    if (!file.exists()) {
      cf.thenRun(() -> {
        try {
          downloadHead(name);
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
    }
    
    cf.completeAsync(() -> {
      try {
        return ImageIO.read(file);
      } catch (IOException e) {
        e.printStackTrace();
        return null;
      }
    });
    
    return cf;
  }
  
  public List<Component> getHeadLinesSync(String name) {
    var list = new ArrayList<Component>();
    var img = getPlayerFaceSync(name);
    for (int h = 0; h < img.getHeight(); h++) {
      var text = Component.empty();
      for (int w = 0; w < img.getWidth(); w++) {
        text = text.append(Component.text(filler, TextColor.color(img.getRGB(w, h))));
      }
      list.add(text);
    }
    return list;
  }
  
  public BufferedImage getPlayerFaceSync(String name) {
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
  
  String resolvePath(String name) {
    return plugin.getDataFolder().toPath().resolve("heads").resolve(name + ".png").toAbsolutePath().toString();
  }
  
  File resolveFile(String name) {
    return plugin.getDataFolder().toPath().resolve(name + ".png").toFile();
  }
}
