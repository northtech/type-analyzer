package dk.northtech.datawash.Analyzer.DataTypeScanning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class BooleanScanner implements DataTypeScanner {
  private static final Logger LOGGER = LoggerFactory.getLogger(BooleanScanner.class);
  
  @Override
  public boolean scan(Object value) {
    if (value instanceof Boolean) {
      return true;
    }
    
    try {
      Integer valueAsInteger;
      valueAsInteger = (Integer) value;
  
      if (valueAsInteger.equals(0) || valueAsInteger.equals(1)) {
        return true;
      }
    }
    catch (ClassCastException cce) {
    
    }
    
    String valueAsString = value.toString();
    
    if (Boolean.parseBoolean(valueAsString)) {
      return true;
    }
    
    return false;
  }
  
  @Override
  public Class getType() {
    return Boolean.class;
  }
}
