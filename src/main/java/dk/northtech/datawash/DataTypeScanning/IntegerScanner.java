package dk.northtech.datawash.DataTypeScanning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class IntegerScanner extends DataTypeScanner<Integer> {
  private static final Logger LOGGER = LoggerFactory.getLogger(IntegerScanner.class);
  
  public IntegerScanner() {
    super(Integer.class);
  }
  
  
  @Override
  public boolean scan(Object value) {
    LOGGER.debug("Testing if value: {} is an Integer", value);
    
    if (value instanceof Integer) {
      LOGGER.debug("value: {} is instance of Integer", value);
      return true;
    }
    
    Number valueAsNumber = null;
    Integer valueAsInteger = null;
    Double valueAsDouble = null;
    
    try {
      valueAsNumber = (Number) value;
      valueAsInteger = valueAsNumber.intValue();
      valueAsDouble = valueAsNumber.doubleValue();
    }
    catch (ClassCastException cce) {
      LOGGER.debug("value: {} not castable to Number", value);
    }
    
    if (valueAsNumber != null && valueAsInteger != null && valueAsInteger.doubleValue() == valueAsDouble) {
      LOGGER.debug("value: {} as Integer has same value if Double", value);
      return true;
    }
    
    CharSequence valueAsCharSequence = null;
    String valueAsString = null;
    Integer parsedInteger = null;
    
    try {
      valueAsCharSequence = (CharSequence) value;
      valueAsString = valueAsCharSequence.toString();
    }
    catch (ClassCastException cce) {
      LOGGER.debug("" + value.toString() + " not castable to CharSequence");
    }
    
    if (valueAsString != null) {
      try {
        parsedInteger = Integer.parseInt(valueAsString);
      }
      catch (NumberFormatException nfe) {
        LOGGER.debug("valueAsString: {} not parsable to Integer", valueAsString);
      }
      
      if (parsedInteger != null) {
        return true;
      }
    }
    
    LOGGER.debug("value: {} is not an Integer", value);
    return false;
  }
  
  @Override
  public Integer convert(Object value) {
    return null;
  }
  
  
}
