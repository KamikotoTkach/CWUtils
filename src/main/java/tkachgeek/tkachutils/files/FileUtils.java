package tkachgeek.tkachutils.files;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileUtils {
  /**
   * @return пустую строку, если не удалось прочитать файл или его нет
   */
  public static String readString(Path path) {
    try {
      return Files.readString(path, StandardCharsets.UTF_8);
    } catch (IOException ignored) {
    }
    return "";
  }
  
  public static void writeString(Path path, String text) {
    try {
      if (!Files.exists(path)) {
        com.google.common.io.Files.createParentDirs(path.toFile());
      }
      Files.writeString(path, text, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public static void downloadFileTo(String source, String destination) throws IOException {
    URL url = new URL(source);
    
    InputStream is = url.openStream();
    OutputStream os = new FileOutputStream(destination);
    
    byte[] b = new byte[2048];
    int length;
    
    while ((length = is.read(b)) != -1) {
      os.write(b, 0, length);
    }
    
    is.close();
    os.close();
  }
}
