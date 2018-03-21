package dk.northtech.datawash.Analyzer.DataTypeScanning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class BooleanScanner extends DataTypeScanner<Boolean> {
  private static final Logger LOGGER = LoggerFactory.getLogger(BooleanScanner.class);
  
  
  @Override
  public boolean scan(Object value) {
    return false;
  }
  
  @Override
  public Boolean convert(Object value) {
    return null;
  }
}
