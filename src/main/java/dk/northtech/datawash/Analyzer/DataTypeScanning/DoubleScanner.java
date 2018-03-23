package dk.northtech.datawash.Analyzer.DataTypeScanning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class DoubleScanner implements DataTypeScanner {
  private static final Logger LOGGER = LoggerFactory.getLogger(DoubleScanner.class);
  
  
  @Override
  public boolean scan(Object value) {
    if (value instanceof Double) {
      return true;
    }
    
    try {
      Number  valueAsNumber;
      Double  valueAsDouble;
      
      valueAsNumber = (Number) value;
      valueAsDouble = valueAsNumber.doubleValue();
      
      if (valueAsNumber.equals(valueAsDouble)) {
        return true;
      }
    }
    catch (ClassCastException cce) {
    
    }
  
    try {
      CharSequence valueAsCharSequence;
      String       valueAsString;
    
      valueAsCharSequence = (CharSequence) value;
      valueAsString = valueAsCharSequence.toString();
    
    
      Double.parseDouble(valueAsString);
    
      return true; // wont be reached if .parseDouble() throws exception
    
    
    }
    catch (RuntimeException re) {
    
    }
    
    return false;
  }
  
  @Override
  public Class getType() {
    return Double.class;
  }
}
