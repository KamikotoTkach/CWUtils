package tkachgeek.tkachutils.items.activeItem;

public enum ItemAction {
  LEFT_CLICK_BLOCK,
  /**
   * Right-clicking a block
   */
  RIGHT_CLICK_BLOCK,
  /**
   * Left-clicking the air
   */
  LEFT_CLICK_AIR,
  /**
   * Right-clicking the air
   */
  RIGHT_CLICK_AIR,
  NOTHING,
  /**
   * All of the items on the clicked slot are moved to the cursor.
   */
  PICKUP_ALL,
  /**
   * Some of the items on the clicked slot are moved to the cursor.
   */
  PICKUP_SOME,
  /**
   * Half of the items on the clicked slot are moved to the cursor.
   */
  PICKUP_HALF,
  /**
   * One of the items on the clicked slot are moved to the cursor.
   */
  PICKUP_ONE,
  /**
   * All of the items on the cursor are moved to the clicked slot.
   */
  PLACE_ALL,
  /**
   * Some of the items from the cursor are moved to the clicked slot
   * (usually up to the max stack size).
   */
  PLACE_SOME,
  /**
   * A single item from the cursor is moved to the clicked slot.
   */
  PLACE_ONE,
  /**
   * The clicked item and the cursor are exchanged.
   */
  SWAP_WITH_CURSOR,
  /**
   * The entire cursor item is dropped.
   */
  DROP_ALL_CURSOR,
  /**
   * One item is dropped from the cursor.
   */
  DROP_ONE_CURSOR,
  /**
   * The entire clicked slot is dropped.
   */
  DROP_ALL_SLOT,
  /**
   * One item is dropped from the clicked slot.
   */
  DROP_ONE_SLOT,
  /**
   * The item is moved to the opposite inventory if a space is found.
   */
  MOVE_TO_OTHER_INVENTORY,
  /**
   * The clicked item is moved to the hotbar, and the item currently there
   * is re-added to the player's inventory.
   * <p>
   * The hotbar includes the player's off hand.
   */
  HOTBAR_MOVE_AND_READD,
  /**
   * The clicked slot and the picked hotbar slot are swapped.
   * <p>
   * The hotbar includes the player's off hand.
   */
  HOTBAR_SWAP,
  /**
   * A max-size stack of the clicked item is put on the cursor.
   */
  CLONE_STACK,
  /**
   * The inventory is searched for the same material, and they are put on
   * the cursor up to {@link org.bukkit.Material#getMaxStackSize()}.
   */
  COLLECT_TO_CURSOR,
  /**
   * An unrecognized ClickType.
   */
  UNKNOWN,
  LEFT,
  /**
   * Holding shift while pressing the left mouse button.
   */
  SHIFT_LEFT,
  /**
   * The right mouse button.
   */
  RIGHT,
  /**
   * Holding shift while pressing the right mouse button.
   */
  SHIFT_RIGHT,
  /**
   * Clicking the left mouse button on the grey area around the inventory.
   */
  WINDOW_BORDER_LEFT,
  /**
   * Clicking the right mouse button on the grey area around the inventory.
   */
  WINDOW_BORDER_RIGHT,
  /**
   * The middle mouse button, or a "scrollwheel click".
   */
  MIDDLE,
  /**
   * One of the number keys 1-9, correspond to slots on the hotbar.
   */
  NUMBER_KEY,
  /**
   * Pressing the left mouse button twice in quick succession.
   */
  DOUBLE_CLICK,
  /**
   * The "Drop" key (defaults to Q) in inventory.
   */
  DROP,
  /**
   * Holding Ctrl while pressing the "Drop" key (defaults to Q) in inventory.
   */
  CONTROL_DROP,
  /**
   * Any action done with the Creative inventory open.
   */
  CREATIVE,
  /**
   * The "swap item with offhand" key (defaults to F).
   */
  SWAP_OFFHAND,
  /**
   * Drop item in world (with closed inventory press the "Drop" key (defaults to Q)
   */
  DROP_ITEM_EVENT,
}
