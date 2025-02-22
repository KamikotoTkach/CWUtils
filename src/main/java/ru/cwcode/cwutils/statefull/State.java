package ru.cwcode.cwutils.statefull;

public interface State<H extends StateHolder<H, S>, S extends State<H, S>> {
  void onEnable(H holder);
  
  void onDisable(H holder);
}