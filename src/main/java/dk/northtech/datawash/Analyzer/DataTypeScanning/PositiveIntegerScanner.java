package dk.northtech.datawash.Analyzer.DataTypeScanning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PositiveIntegerScanner extends DataTypeScanner<PositiveIntegerScanner.PositiveInteger> {
  private static final Logger LOGGER = LoggerFactory.getLogger(PositiveIntegerScanner.class);
  
  
  @Override
  public boolean scan(Object value) {
    return false;
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
