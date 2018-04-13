package dk.northtech.typeanalyzer.Analyzer.DataTypeScanning;

import dk.northtech.typeanalyzer.Analyzer.DataTypeScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class IntegerScanner implements DataTypeScanner {
  private static final Logger LOGGER = LoggerFactory.getLogger(IntegerScanner.class);


  @Override
  public boolean scan(Object value) {
    if (value instanceof Integer) {
      return true;
    }

    try {
      Number  valueAsNumber;
      Integer valueAsInteger;
      Double  valueAsDouble;

      valueAsNumber = (Number) value;
      valueAsInteger = valueAsNumber.intValue();
      valueAsDouble = valueAsNumber.doubleValue();

      if (valueAsInteger.doubleValue() == valueAsDouble) {
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


      Integer.parseInt(valueAsString);

      return true; // wont be reached if .parseInt() throws exception


    }
    catch (RuntimeException re) {

    }

    return false;
  }

  @Override
  public Class getType() {
    return Integer.class;
  }
}
