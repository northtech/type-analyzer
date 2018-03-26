package dk.northtech.datawash.Analyzer.DataTypeScanning;

import dk.northtech.datawash.Analyzer.DataTypeScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class StringScanner implements DataTypeScanner {
  private static final Logger LOGGER = LoggerFactory.getLogger(StringScanner.class);
  
  
  @Override
  public boolean scan(Object value) {
    if (value instanceof String) {
      return true;
    }
    
    try {
      CharSequence valueAsCharSequence = (CharSequence) value;
      String       valueAsString       = valueAsCharSequence.toString();
      
      if (value.toString().equals(valueAsString)) {
        return true;
      }
    }
    catch (ClassCastException cce) {
    
    }
    
    return false;
  }
  
  @Override
  public Class getType() {
    return String.class;
  }
}
