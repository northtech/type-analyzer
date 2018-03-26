package dk.northtech.datawash.Analyzer;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * This interface defines two methods. The first is for {@code ColumnAnalyzers} to use when scanning Objects, the
 * other is for the {@code DataTypeAnalyzerBuilder} to use when setting scanners.
 */
@ParametersAreNonnullByDefault
public interface DataTypeScanner {
  /**
   * @param value the Object to scan.
   *
   * @return true if the given Object is of the type that this scanner scans for.
   */
  boolean scan(Object value);
  
  /**
   * Gets the specific type that a scanner scans for.
   *
   * @return the Class that this scanner scans for.
   */
  Class getType();
}
