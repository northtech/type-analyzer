package dk.northtech.datawash.Analyzer;

import dk.northtech.datawash.Analyzer.DataTypeScanning.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.time.Instant;
import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * A mutable builder for configuring DataTypeAnalyzer objects.
 */
@ParametersAreNonnullByDefault
public class DataTypeAnalyzerBuilder {
  private static final Logger LOGGER = LoggerFactory.getLogger(DataTypeAnalyzerBuilder.class);
  
  private final Collection<List<Class>>                  hierarchies       = new LinkedList<>();
  private final Map<Class, DataTypeScanner>              scanners          = new HashMap<>();
  private final Map<String, Collection<List<Class>>>     columnHierarchies = new HashMap<>();
  private final Map<String, Map<Class, DataTypeScanner>> columnScanners    = new HashMap<>();
  private final Map<String, Double>                      columnTolerances  = new HashMap<>();
  private final Map<String, Boolean>                     columnNullable    = new HashMap<>();
  private       Double                                   tolerance         = 0.0;
  private       Boolean                                  nullable          = true;
  
  
  public DataTypeAnalyzerBuilder() {
    
    // Default scanners for basic types
    scanners.put(Boolean.class, new BooleanScanner());
    scanners.put(Integer.class, new IntegerScanner());
    scanners.put(Double.class, new DoubleScanner());
    scanners.put(Number.class, new NumberScanner());
    scanners.put(Instant.class, new IsoDateScanner());
    scanners.put(String.class, new StringScanner());
    
    // Additional examples
    scanners.put(PositiveIntegerScanner.PositiveInteger.class, new PositiveIntegerScanner());
    scanners.put(WordScanner.Word.class, new WordScanner());
  }
  
  /**
   * Specify a {@code DataTypeScanner} to use for a specific data type.
   *
   * @param scanner the scanner object that will be used to scan for this data type
   *
   * @return this object, to be able to chain method calls.
   */
  public DataTypeAnalyzerBuilder setScanner(DataTypeScanner scanner) {
    scanners.put(scanner.getType(), scanner);
    return this;
  }
  
  /**
   * For a specific column, specify a {@code DataTypeScanner} to use for a specific data type.
   *
   * @param columnId specific column ID this scanner will apply to.
   * @param scanner  the scanner object that will be used to scan for this data type.
   *
   * @return this object, to be able to chain method calls.
   */
  public DataTypeAnalyzerBuilder setScanner(String columnId, DataTypeScanner scanner) {
    columnScanners.getOrDefault(columnId, new HashMap<>()).put(scanner.getType(), scanner);
    return this;
  }
  
  /**
   * Set whether values are nullable or not.
   *
   * @param nullable whether or not to accept null values as instances of data types.
   *
   * @return this object, to be able to chain method calls..
   */
  public DataTypeAnalyzerBuilder allowNullable(Boolean nullable) {
    this.nullable = nullable;
    return this;
  }
  
  /**
   * For a specific column, set whether values are nullable or not.
   *
   * @param columnId the specific column this setting will apply to.
   * @param nullable whether or not to accept null values as instances of data types for this column.
   *
   * @return this object, to be able to chain method calls.
   */
  public DataTypeAnalyzerBuilder allowNullable(String columnId, Boolean nullable) {
    columnNullable.put(columnId, nullable);
    return this;
  }
  
  /**
   * Set the allowed fraction which the amount of values of a certain type are allowed to differ from 100%.
   *
   * @param tolerance the amount of variance the analyzer will allow and still pick a certain data type as the best fit.
   *                  A number between 0.0 and 1.0, representing the allowed variance as a fraction. 0.0 is
   *                  zero-tolerance.
   *
   * @return this object, to be able to chain method calls.
   */
  public DataTypeAnalyzerBuilder setTolerance(Double tolerance) {
    checkArgument(tolerance <= 1.0 && tolerance >= 0);
    this.tolerance = tolerance;
    return this;
  }
  
  /**
   * For a specific column, set the allowed fraction which the amount of values of a certain type are allowed to differ
   * from 100%.
   *
   * @param columnId  the specific column this setting will apply to.
   * @param tolerance the amount of variance the analyzer will allow and still pick a certain data type as the best fit.
   *                  A number between 0.0 and 1.0, representing the allowed variance as a fraction. 0.0 is
   *                  zero-tolerance.
   *
   * @return this object, to be able to chain method calls.
   */
  public DataTypeAnalyzerBuilder setTolerance(String columnId, Double tolerance) {
    checkArgument(tolerance <= 1.0 && tolerance >= 0);
    columnTolerances.put(columnId, tolerance);
    return this;
  }
  
  /**
   * Add a list of data types to scan for. The list must be in sorted order of ascending generality. Ie. a data type
   * in the list must always be a super type of any data type that came before it in the same list.
   *
   * @param hierarchy the hierarchy to scan for.
   *
   * @return this object, to be able to chain method calls.
   */
  public DataTypeAnalyzerBuilder addHierarchy(List<Class> hierarchy) {
    this.hierarchies.add(hierarchy);
    return this;
  }
  
  /**
   * For a specific column, add a list of data types to scan for. The list must be in sorted order of ascending
   * generality. Ie. a data type
   * in the list must always be a super type of any data type that came before it in the same list.
   *
   * @param columnId  the specific column this setting will apply to.
   * @param hierarchy the hierarchy to scan for.
   *
   * @return this object, to be able to chain method calls.
   */
  public DataTypeAnalyzerBuilder addHierarchy(String columnId, List<Class> hierarchy) {
    columnHierarchies.putIfAbsent(columnId, new LinkedList<>());
    columnHierarchies.get(columnId).add(hierarchy);
    return this;
  }
  
  /**
   * Add a list of data types to scan for. The list must be in sorted order of ascending generality. Ie. a data type
   * in the list must always be a super type of any data type that came before it in the same list.
   *
   * @param types the vararg of types which will be turned into a list.
   *
   * @return this object, to be able to chain method calls.
   */
  public DataTypeAnalyzerBuilder addHierarchy(Class... types) {
    this.hierarchies.add(Arrays.asList(types));
    return this;
  }
  
  /**
   * Add a list of data types to scan for. The list must be in sorted order of ascending generality. Ie. a data type
   * in the list must always be a super type of any data type that came before it in the same list.
   *
   * @param columnId the specific column this setting will apply to.
   * @param types    the vararg of types which will be turned into a list.
   *
   * @return this object, to be able to chain method calls.
   */
  public DataTypeAnalyzerBuilder addHierarchy(String columnId, Class... types) {
    columnHierarchies.putIfAbsent(columnId, new LinkedList<>());
    columnHierarchies.get(columnId).add(Arrays.asList(types));
    return this;
  }
  
  /**
   * Finally build your {@code DataTypeAnalyzer}
   *
   * @return a {@code DataTypeAnalyzer} configured with this {@code DataTypeAnalyzerBuilder}'s properties.
   */
  public DataTypeAnalyzer build() {
    if (this.hierarchies.isEmpty()) {
      throw new IllegalStateException("Builder has not been supplied with a hierarchy of types to scan for");
    }
    for (List<Class> hierarchy : hierarchies) {
      for (Class type : hierarchy) {
        if (!scanners.containsKey(type)) {
          throw new IllegalStateException("Builder does not have a scanner for type: " + type.toString());
        }
      }
    }
    
    return new DataTypeAnalyzer(this.hierarchies,
                                this.columnHierarchies,
                                this.scanners,
                                this.columnScanners,
                                this.tolerance,
                                this.columnTolerances,
                                this.nullable,
                                this.columnNullable);
  }
}
