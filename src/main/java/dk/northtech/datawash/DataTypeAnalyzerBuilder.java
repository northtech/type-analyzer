package dk.northtech.datawash;

import dk.northtech.datawash.DataTypeScanning.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.time.Instant;
import java.util.*;

@ParametersAreNonnullByDefault
public class DataTypeAnalyzerBuilder {
  private static final Logger LOGGER = LoggerFactory.getLogger(DataTypeAnalyzerBuilder.class);
  
  private final Collection<List<Class>> hierarchies = new LinkedList<>();
  private final Map<Class, DataTypeScanner> scanners = new HashMap<>();
  private final Map<String, Collection<List<Class>>> columnHierarchies = new HashMap<>();
  private final Map<String, Map<Class, DataTypeScanner>> columnScanners = new HashMap<>();
  private final Map<String, Double> columnTolerances = new HashMap<>();
  private final Map<String, Boolean> columnNullable = new HashMap<>();
  private Double tolerance = 0.0;
  private Boolean nullable = true;
  
  
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
  
  public DataTypeAnalyzerBuilder setScanner(Class type, DataTypeScanner scanner) {
    scanners.put(type, scanner);
    return this;
  }
  
  public DataTypeAnalyzerBuilder setScanner(String columnId, Class type, DataTypeScanner scanner) {
    columnScanners.getOrDefault(columnId, new HashMap<>()).put(type, scanner);
    return this;
  }
  
  public DataTypeAnalyzerBuilder allowNullable(Boolean nullable) {
    this.nullable = nullable;
    return this;
  }
  
  public DataTypeAnalyzerBuilder allowNullable(String columnId, Boolean nullable) {
    columnNullable.put(columnId, nullable);
    return this;
  }
  
  public DataTypeAnalyzerBuilder setTolerance(Double tolerance) {
    this.tolerance = tolerance;
    return this;
  }
  
  public DataTypeAnalyzerBuilder setTolerance(String columnId, Double tolerance) {
    columnTolerances.put(columnId, tolerance);
    return this;
  }
  
  public DataTypeAnalyzerBuilder addHierarchy(List<Class> hierarchy) {
    this.hierarchies.add(hierarchy);
    return this;
  }
  
  public DataTypeAnalyzerBuilder addHierarchy(String columnId, List<Class> hierarchy) {
    columnHierarchies.putIfAbsent(columnId, new LinkedList<>());
    columnHierarchies.get(columnId).add(hierarchy);
    return this;
  }
  
  public DataTypeAnalyzerBuilder addHierarchy(Class... types) {
    this.hierarchies.add(Arrays.asList(types));
    return this;
  }
  
  public DataTypeAnalyzerBuilder addHierarchy(String columnId, Class... types) {
    columnHierarchies.putIfAbsent(columnId, new LinkedList<>());
    columnHierarchies.get(columnId).add(Arrays.asList(types));
    return this;
  }
  
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
    
    return new DataTypeAnalyzer(
      this.hierarchies,
      this.columnHierarchies,
      this.scanners,
      this.columnScanners,
      this.tolerance,
      this.columnTolerances,
      this.nullable,
      this.columnNullable);
  }
}
