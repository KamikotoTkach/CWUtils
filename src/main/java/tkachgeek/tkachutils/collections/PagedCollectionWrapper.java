package tkachgeek.tkachutils.collections;

import java.util.Collections;
import java.util.List;

public class PagedCollectionWrapper<T> {
  List<T> entries;
  int pageSize;
  
  public PagedCollectionWrapper(List<T> entries, int pageSize) {
    this.entries = entries;
    this.pageSize = pageSize;
  }
  
  public List<T> getPage(int page) {
    int from = pageSize * (page - 1), to = pageSize * page;
    
    if (from < 0 || to < 0) return Collections.emptyList();
    
    return entries.subList(Math.min(from, entries.size()), Math.min(to, entries.size()));
  }
}
