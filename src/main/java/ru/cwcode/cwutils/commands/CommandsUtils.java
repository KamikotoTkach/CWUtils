package ru.cwcode.cwutils.commands;

import java.util.Collection;
import java.util.Optional;

public class CommandsUtils {
  public static boolean containsCommand(Collection<String> commands, String message) {
    return getCommand(commands, message).isPresent();
  }
  
  public static Optional<String> getCommand(Collection<String> commands, String message) {
    for (String command : commands) {
      if (command.contains("*")) {
        command = command.replace("*", "");
        
        if (message.startsWith(command) || message.startsWith("/" + command)) {
          return Optional.of(command);
        }
        
        String[] cmd = message.split(":", 2);
        if (cmd.length > 1) {
          if (cmd[1].startsWith(command) || command.startsWith("/") && cmd[1].startsWith(command.substring(1))) {
            return Optional.of(command);
          }
        }
        
        continue;
      }
      
      String cmd = message.split(" ")[0];
      
      if (cmd.equals(command) || cmd.equals("/" + command)) {
        return Optional.of(command);
      }
      
      String[] cmdAlias = cmd.split(":", 2);
      if (cmdAlias.length > 1) {
        if (cmdAlias[1].equals(command) || command.startsWith("/") && cmdAlias[1].equals(command.substring(1))) {
          return Optional.of(command);
        }
      }
    }
    
    return Optional.empty();
  }
}
