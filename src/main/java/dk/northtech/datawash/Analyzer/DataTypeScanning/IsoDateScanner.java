package dk.northtech.datawash.Analyzer.DataTypeScanning;

import dk.northtech.gsonextensions.javatypes.temporal.InstantTypeAdapterFactory;
import dk.northtech.gsonextensions.javatypes.temporal.ZonedDateTimeTypeAdapterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.time.Instant;
import java.time.format.DateTimeParseException;

@ParametersAreNonnullByDefault
public class IsoDateScanner extends DataTypeScanner<Instant> {
  private static final Logger LOGGER = LoggerFactory.getLogger(IsoDateScanner.class);
  
  
  @Override
  public boolean scan(Object value) {
    try {
      return (InstantTypeAdapterFactory.parse(value) != null) ||
             (ZonedDateTimeTypeAdapterFactory.parse(value.toString()) != null);
    }
    catch (DateTimeParseException e) {
      return false;
    }
  }
}
