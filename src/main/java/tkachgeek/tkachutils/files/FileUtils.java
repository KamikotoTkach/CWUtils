package tkachgeek.tkachutils.files;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileUtils {
  /**
   *
   * @return пустую строку, если не удалось прочитать файл или его нет
   */
  static String readString(Path path) {
    try {
      return Files.readString(path, StandardCharsets.UTF_8);
    } catch (IOException ignored) {
    }
    return "";
  }
  
  static void writeString(Path path, String text) {
    try {
      if (!Files.exists(path)) {
        com.google.common.io.Files.createParentDirs(path.toFile());
      }
      Files.writeString(path, text, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
