package dk.northtech.datawash;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class Pair {
  private static final Logger LOGGER = LoggerFactory.getLogger(Pair.class);
  
  public final String key;
  public final Object value;
  
  Pair(String key, @Nullable Object value) {
    this.key = key;
    this.value = value;
  }
}
