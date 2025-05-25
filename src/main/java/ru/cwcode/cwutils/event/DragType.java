package ru.cwcode.cwutils.event;

import org.bukkit.event.inventory.InventoryDragEvent;

public enum DragType {
  INVENTORY,
  OWN_INVENTORY,
  ALL_INVENTORIES;
  
  public static DragType getDragType(InventoryDragEvent event) {
    DragType dragType = null;
    int size = event.getInventory().getSize();
    
    for (int slot : event.getRawSlots()) {
      boolean isOwnInventory = slot >= size;
      if (isOwnInventory) {
        if (dragType == DragType.INVENTORY) return DragType.ALL_INVENTORIES;
        dragType = DragType.OWN_INVENTORY;
      } else {
        if (dragType == DragType.OWN_INVENTORY) return DragType.ALL_INVENTORIES;
        dragType = DragType.INVENTORY;
      }
    }
    
    return dragType;
  }
}
