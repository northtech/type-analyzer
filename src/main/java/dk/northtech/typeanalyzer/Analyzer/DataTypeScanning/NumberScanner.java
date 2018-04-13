package dk.northtech.typeanalyzer.Analyzer.DataTypeScanning;

import dk.northtech.typeanalyzer.Analyzer.DataTypeScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class NumberScanner implements DataTypeScanner {
  private static final Logger LOGGER = LoggerFactory.getLogger(NumberScanner.class);


  @Override
  public boolean scan(Object value) {
    if (value instanceof Number) {
      return true;
    }

    try {
      Number valueAsNumber = (Number) value;

      return true; // Not reached in case of exception

    }
    catch (ClassCastException cce) {

    }

    return false;
  }

  @Override
  public Class getType() {
    return Number.class;
  }
}
