package ru.cwcode.cwutils.bounceable;

import org.bukkit.Location;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface BounceableFallingBlock extends Bounceable<FallingBlock> {
  @Override
  default Class<FallingBlock> getEntityClass() {
    return FallingBlock.class;
  }
  
  @Override
  default FallingBlock spawnEntity(Location location, Player player, ItemStack itemStack) {
    return location.getWorld().spawnFallingBlock(location, itemStack.getType().createBlockData());
  }
}
