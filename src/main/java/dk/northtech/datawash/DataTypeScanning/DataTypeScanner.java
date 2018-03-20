package dk.northtech.datawash.DataTypeScanning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class DataTypeScanner<T> {
  private static final Logger LOGGER = LoggerFactory.getLogger(IntegerScanner.class);
  
  
  public abstract boolean scan(Object value);
  
  /**
   Assumes the argument is of a compatible format
   Converts the value to the correct type
   
   @param value object to be converted
   @return argument converted to scanner's type
   */
  public T convert(Object value) {
    LOGGER.warn("convert() is not implemented for this scanner");
    return null;
  }
}
