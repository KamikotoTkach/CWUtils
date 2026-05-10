package ru.cwcode.cwutils.commands;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@UtilityClass
public class CommandsUtils {
  public boolean containsCommand(Collection<String> commands, String message) {
    return getCommand(commands, message).isPresent();
  }
  
  public Optional<String> getCommand(Collection<String> commands, String rawMessage) {
    String message = normalizeMessage(rawMessage);
    String[] splitMessage = message.split(" ");
    
    for (String command : commands) {
      if (command.contains("*")) {
        String cleanCommand = command.replace("*", "");
        if (isMatching(message, cleanCommand, true)) return Optional.of(command);
        
        String[] cmdParts = message.split(":", 2);
        if (cmdParts.length > 1 && isMatching(cmdParts[1], cleanCommand, true)) return Optional.of(command);
        
        continue;
      }
      
      int wordCount = command.split(" ").length;
      if (wordCount > splitMessage.length) continue;
      
      String cmd = String.join(" ", Arrays.copyOfRange(splitMessage, 0, wordCount));
      if (isMatching(cmd, command, false)) return Optional.of(command);
      
      String[] cmdAlias = cmd.split(":", 2);
      if (cmdAlias.length > 1 && isMatching(cmdAlias[1], command, false)) return Optional.of(command);
    }
    
    return Optional.empty();
  }
  
  private boolean isMatching(String input, String target, boolean isWildcard) {
    String cleanInput = input.startsWith("/") ? input.substring(1) : input;
    String cleanTarget = target.startsWith("/") ? target.substring(1) : target;
    
    cleanInput = cleanInput.toLowerCase();
    cleanTarget = cleanTarget.toLowerCase();
    
    return isWildcard ? cleanInput.startsWith(cleanTarget) : cleanInput.equals(cleanTarget);
  }
  
  private String normalizeMessage(String message) {
    if (message == null || message.trim().isEmpty()) return message;
    
    String[] parts = message.split(" ", 2);
    String firstArg = parts[0];
    
    boolean hasSlash = firstArg.startsWith("/");
    String labelWithNamespace = hasSlash ? firstArg.substring(1) : firstArg;
    
    String[] namespaceParts = labelWithNamespace.split(":", 2);
    String label = namespaceParts.length > 1 ? namespaceParts[1] : labelWithNamespace;
    
    Command bukkitCommand = Bukkit.getCommandMap().getCommand(label);
    if (bukkitCommand == null) return message;
    
    String normalized = (hasSlash ? "/" : "") + bukkitCommand.getName();
    return parts.length > 1 ? normalized + " " + parts[1] : normalized;
  }
}