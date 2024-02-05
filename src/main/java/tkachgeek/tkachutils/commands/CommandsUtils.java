package tkachgeek.tkachutils.commands;

import java.util.Collection;

public class CommandsUtils {
  public static boolean containsCommand(Collection<String> commands, String message) {
    for (String command : commands) {
      if (message.startsWith(command) || message.startsWith("/" + command)) {
        return true;
      }

      String[] cmd = message.split(":", 2);
      if (cmd.length > 1) {
        if (cmd[1].startsWith(command) || command.startsWith("/") && cmd[1].startsWith(command.substring(1))) {
          return true;
        }
      }
    }

    return false;
  }
}
