package dk.northtech.datawash.DataTypeScanning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class StringScanner extends DataTypeScanner<String> {
  private static final Logger LOGGER = LoggerFactory.getLogger(StringScanner.class);
  
  
  @Override
  public boolean scan(Object value) {
    LOGGER.debug("Testing if value: {} is a String", value);
    if (value instanceof String) {
      LOGGER.debug("value: {} is instance of String", value);
      return true;
    }
    
    LOGGER.debug("value: {} is not a String", value);
    return false;
  }
  
  @Override
  public String convert(Object value) {
    return (String) value;
  }
}
