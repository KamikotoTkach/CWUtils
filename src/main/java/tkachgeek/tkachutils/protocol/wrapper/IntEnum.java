package tkachgeek.tkachutils.protocol.wrapper;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class IntEnum {
  protected BiMap<Integer, String> members = HashBiMap.create();
  
  public IntEnum() {
    this.registerAll();
  }
  
  public boolean hasMember(int id) {
    return this.members.containsKey(id);
  }
  
  public Integer valueOf(String name) {
    return this.members.inverse().get(name);
  }
  
  public String getDeclaredName(Integer id) {
    return this.members.get(id);
  }
  
  public Set<Integer> values() {
    return new HashSet<>(this.members.keySet());
  }
  
  protected void registerAll() {
    try {
      Field[] fields = this.getClass().getFields();
      
      for (Field entry : fields) {
        if (entry.getType().equals(Integer.TYPE)) {
          this.registerMember(entry.getInt(this), entry.getName());
        }
      }
    } catch (IllegalArgumentException | IllegalAccessException var5) {
      var5.printStackTrace();
    }
  }
  
  protected void registerMember(int id, String name) {
    this.members.put(id, name);
  }
}