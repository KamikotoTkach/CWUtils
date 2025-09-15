package ru.cwcode.cwutils.items.compare;

import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;

@UtilityClass
public class CompareServiceProvider {
  private final List<CompareService> services = List.of(
    new PotionCompareService(),
    new ItemCompareService()
  );
  
  public Optional<CompareService> find(ItemStack itemStack) {
    if (itemStack == null) return Optional.empty();
    for (CompareService service : services) {
      if (!service.supports(itemStack)) continue;
      return Optional.of(service);
    }
    
    return Optional.empty();
  }
}
