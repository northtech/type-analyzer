package dk.northtech.datawash.Analyzer.stages;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import dk.northtech.datawash.Analyzer.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

@ParametersAreNonnullByDefault
public class DataState<SELF extends DataState<SELF>> extends Stage<SELF> {
  private static final Logger LOGGER = LoggerFactory.getLogger(DataState.class);
  
  @ProvidedScenarioState
  List<Map<String, Object>> dataList;

  
  public SELF a_memory_list_of_Map_objects() {
    dataList = new ArrayList<>();
    return self();
  }
  
  public SELF the_list_is_HUGE(int magnitude) {
  
    for (int i = 0; i < magnitude; i++) {
      dataList.addAll(dataList);
    }
    
    return self();
  }
  
  public SELF the_list_contains_object(String k, @Nullable Object v) {
    Map<String, Object> o = new HashMap<>();
    o.put(k, v);
    dataList.add(o);
    return self();
  }
  
  public SELF the_list_contains_object(String k1, @Nullable Object v1, String k2, @Nullable Object v2) {
    Map<String, Object> o = new HashMap<>();
    o.put(k1, v1);
    o.put(k2, v2);
    dataList.add(o);
    return self();
  }
  
  public SELF the_list_contains_object(String k1, @Nullable Object v1, String k2, @Nullable Object v2, String k3, @Nullable Object v3) {
    Map<String, Object> o = new HashMap<>();
    o.put(k1, v1);
    o.put(k2, v2);
    o.put(k3, v3);
    dataList.add(o);
    return self();
  }
  
  public SELF the_list_contains_object(Pair... pairs) {
    Map<String, Object> o = new HashMap<>();
    for (Pair pair : pairs) {
      o.put(pair.key, pair.value);
    }
    dataList.add(o);
    return self();
  }
  
  
  /**
   @param vk must be even amount
   @return
   */
  public SELF the_list_contains_object(@Nullable Object... vk) {
    checkArgument(vk.length % 2 == 0, "must be even amount of arguments");
    
    for (int i = 0; i < vk.length; i += 2) {
    
    }
    
    return self();
  }
}
