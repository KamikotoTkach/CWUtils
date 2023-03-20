package tkachgeek.tkachutils.items;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class InventorySerializer {
  /**
   * Converts the player inventory to a String array of Base64 strings. First string is the content and second string is the armor.
   *
   * @param playerInventory to turn into an array of strings.
   * @return Array of strings: [ main content, armor content ]
   */
  public static String[] playerInventoryToBase64(PlayerInventory playerInventory) throws IllegalStateException {
    String content = toBase64(playerInventory);
    String armor = itemStackArrayToBase64(playerInventory.getArmorContents());
    
    return new String[]{content, armor};
  }
  
  /**
   * A method to serialize an {@link ItemStack} array to Base64 String.
   *
   * @param items to turn into a Base64 String.
   * @return Base64 string of the items.
   */
  public static String itemStackArrayToBase64(ItemStack[] items) throws IllegalStateException {
    try {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
      
      dataOutput.writeInt(items.length);
      
      for (ItemStack item : items) {
        dataOutput.writeObject(item);
      }
  
      dataOutput.close();
  
      return Base64Coder.encodeLines(outputStream.toByteArray());
    } catch (Exception e) {
      throw new IllegalStateException("Unable to save item stacks.", e);
    }
  }
  
  public static ItemStack[] itemStackArrayFromBase64(String base) {
    try {
      ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(base));
      BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
      
      int len = dataInput.readInt();
      ItemStack[] itemStacks = new ItemStack[len];
      
      for (int i = 0; i < len; i++) {
        itemStacks[i] = (ItemStack) dataInput.readObject();
      }
      
      dataInput.close();
  
      return itemStacks;
    } catch (ClassNotFoundException ignored) {
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return new ItemStack[0];
  }
  
  /**
   * A method to serialize an inventory to Base64 string.
   *
   * @param inventory to serialize
   * @return Base64 string of the provided inventory
   */
  public static String toBase64(Inventory inventory) throws IllegalStateException {
    try {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
      
      dataOutput.writeInt(inventory.getSize());
      
      for (int i = 0; i < inventory.getSize(); i++) {
        dataOutput.writeObject(inventory.getItem(i));
      }
      
      dataOutput.close();
  
      return Base64Coder.encodeLines(outputStream.toByteArray());
    } catch (Exception e) {
      throw new IllegalStateException("Unable to save item stacks.", e);
    }
  }
  
  /**
   * A method to get an {@link Inventory} from an encoded, Base64, string.
   *
   * @param data Base64 string of data containing an inventory.
   * @return Inventory created from the Base64 string.
   */
  public static Inventory fromBase64(String data) throws IOException {
    try {
      ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
      BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
  
      Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt());
      
      for (int i = 0; i < inventory.getSize(); i++) {
        inventory.setItem(i, (ItemStack) dataInput.readObject());
      }
      
      dataInput.close();
  
      return inventory;
    } catch (ClassNotFoundException e) {
      throw new IOException("Unable to decode class type.", e);
    }
  }
}
