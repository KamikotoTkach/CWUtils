package ru.cwcode.cwutils.statefull;

import lombok.Getter;

@Getter
public class StateHolder<H extends StateHolder<H, S>, S extends State<H, S>> {
  private S state;
  
  public void setState(S state) {
    if (this.state != null) {
      this.state.onDisable(self());
    }
    
    this.state = state;
    
    if (this.state != null) {
      this.state.onEnable(self());
    }
  }
  
  @SuppressWarnings("unchecked")
  private H self() {
    return (H) this;
  }
}