package dk.northtech.datawash.Analyzer.DataTypeScanning;

import dk.northtech.datawash.Analyzer.DataTypeScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PositiveIntegerScanner implements DataTypeScanner {
  private static final Logger LOGGER = LoggerFactory.getLogger(PositiveIntegerScanner.class);
  
  
  @Override
  public boolean scan(Object value) {
    IntegerScanner integerScanner = new IntegerScanner();
    
    if (integerScanner.scan(value)) {
      Integer valueAsInteger = (Integer) value;
      return valueAsInteger >= 0;
    }
    
    return false;
  }
  
  @Override
  public Class getType() {
    return PositiveInteger.class;
  }
  
  @ParametersAreNonnullByDefault
  public static class PositiveInteger {
    private static final Logger LOGGER = LoggerFactory.getLogger(PositiveInteger.class);
    
    public final Integer integer;
    
    public PositiveInteger(Integer integer) {
      this.integer = integer;
    }
  }
}
