package ru.cwcode.cwutils.messages;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

public class MessageReturn extends Exception {
  private final Component componentMessage;
  private final boolean styled;
  
  public MessageReturn(Component message) {
    this.componentMessage = message;
    this.styled = true;
  }
  
  public MessageReturn(String text) {
    this.componentMessage = Component.text(text);
    this.styled = false;
  }
  
  public Component getComponentMessage() {
    return this.componentMessage;
  }
  
  public boolean isStyled() {
    return styled;
  }
  
  @Override
  public String getMessage() {
    return ((TextComponent) componentMessage).content();
  }
}