package dk.northtech.datawash.Analyzer.DataTypeScanning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.time.Instant;

@ParametersAreNonnullByDefault
public class IsoDateScanner extends DataTypeScanner<Instant> {
  private static final Logger LOGGER = LoggerFactory.getLogger(IsoDateScanner.class);
  
  
  @Override
  public boolean scan(Object value) {
    return false;
  }
}
