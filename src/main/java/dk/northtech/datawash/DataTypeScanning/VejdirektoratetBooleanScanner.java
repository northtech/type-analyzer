package dk.northtech.datawash.DataTypeScanning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class VejdirektoratetBooleanScanner extends DataTypeScanner<Boolean> {
  private static final Logger LOGGER = LoggerFactory.getLogger(VejdirektoratetBooleanScanner.class);
  
  @Override
  public boolean scan(Object value) {
    return false;
  }
  
}
