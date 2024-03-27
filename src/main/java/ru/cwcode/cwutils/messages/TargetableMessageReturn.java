package ru.cwcode.cwutils.messages;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

import java.util.function.Function;

public class TargetableMessageReturn extends Exception {
  Function<CommandSender, Component> messageGetter;
  
  public TargetableMessageReturn(Function<CommandSender, Component> messageGetter) {
    this.messageGetter = messageGetter;
  }
  
  public Component getMessage(CommandSender sender) {
    return messageGetter.apply(sender);
  }
}