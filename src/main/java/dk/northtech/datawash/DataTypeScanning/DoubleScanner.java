package dk.northtech.datawash.DataTypeScanning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class DoubleScanner extends DataTypeScanner<Double> {
  private static final Logger LOGGER = LoggerFactory.getLogger(DoubleScanner.class);
  
  public DoubleScanner() {
    super(Double.class);
  }
  
  
  @Override
  public boolean scan(Object value) {
    LOGGER.debug("Testing if value: {} is a Double", value);
    if (value instanceof Double) {
      LOGGER.debug("value: {} is instance of Double", value);
      return true;
    }
    
    LOGGER.debug("value: {} is not a Double", value);
    return false;
  }
  
  @Override
  public Double convert(Object value) {
    return null;
  }
}
