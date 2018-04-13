package dk.northtech.typeanalyzer.Analyzer.DataTypeScanning;

import dk.northtech.typeanalyzer.Analyzer.DataTypeScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class VejdirektoratetBooleanScanner implements DataTypeScanner {
  private static final Logger LOGGER = LoggerFactory.getLogger(VejdirektoratetBooleanScanner.class);

  @Override
  public boolean scan(Object value) {
    if (value instanceof Boolean) {
      return true;
    }

    try {
      Integer valueAsInteger;
      valueAsInteger = (Integer) value;

      if (valueAsInteger.equals(1) || valueAsInteger.equals(2)) {
        return true;
      }
    }
    catch (ClassCastException cce) {

    }

    String valueAsString = value.toString();

    if (Boolean.parseBoolean(valueAsString)) {
      return true;
    }

    return false;
  }

  @Override
  public Class getType() {
    return Boolean.class;
  }

}
