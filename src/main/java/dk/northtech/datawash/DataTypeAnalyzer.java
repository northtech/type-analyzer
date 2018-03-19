package dk.northtech.datawash;

import dk.northtech.datawash.DataTypeScanning.DataTypeScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@ParametersAreNonnullByDefault
public class DataTypeAnalyzer {
  private static final Logger LOGGER = LoggerFactory.getLogger(DataTypeAnalyzer.class);
  
  private final Collection<List<Class>> hierarchies = new LinkedList<>();
  private final Map<Class, DataTypeScanner> scanners = new HashMap<>();
  
  private final Map<String, Collection<List<Class>>> columnHierarchies = new HashMap<>();
  private final Map<String, Map<Class, DataTypeScanner>> columnScanners = new HashMap<>();
  private final Map<String, Double> columnTolerance = new HashMap<>();
  private final Map<String, Boolean> columnNullable = new HashMap<>();
  
  private Double tolerance;
  private Boolean nullable;
  
  DataTypeAnalyzer(Collection<List<Class>> hierarchies,
                   Map<String, Collection<List<Class>>> columnHierarchies,
                   Map<Class, DataTypeScanner> scanners,
                   Map<String, Map<Class, DataTypeScanner>> columnScanners,
                   Double tolerance,
                   Map<String, Double> columnTolerance,
                   Boolean nullable,
                   Map<String, Boolean> columnNullable) {
    
    this.hierarchies.addAll(hierarchies);
    this.scanners.putAll(scanners);
    this.tolerance = tolerance;
    this.nullable = nullable;
    
    //TODO: Populate the column specific configuration maps, if arguments are not null, maybe...
    this.columnHierarchies.putAll(columnHierarchies);
    this.columnScanners.putAll(columnScanners);
    this.columnTolerance.putAll(columnTolerance);
    this.columnNullable.putAll(columnNullable);
    
  }
  
  /**
   @param dataSet an iterable of maps from String to Object, each map representing a row in the data
   @return immutable result object
   */
  public Result analyze(Iterable<Map<String, Object>> dataSet) {
    Map<String, ColumnAnalyzer> columnAnalyzers = new HashMap<>();
    
    for (Map<String, Object> row : dataSet) {
      for (String key : row.keySet()) {
        analyzeValue(key, row.get(key), columnAnalyzers);
      }
    }
    
    Set<ColumnResult> columnResults = new HashSet<>();
    for (ColumnAnalyzer columnAnalyzer : columnAnalyzers.values()) {
      columnResults.add(columnAnalyzer.getResult());
    }
    
    Map<String, Integer> amountOfValues = new HashMap<>();
    Map<String, Map<Class, Integer>> typeCounts = new HashMap<>();
    Map<String, Integer> nullCounts = new HashMap<>();
    Map<String, Class> bestFits = new HashMap<>();
    Map<String, Map<Class, Double>> confidences = new HashMap<>();
    
    for (ColumnResult columnResult : columnResults) {
      amountOfValues.put(columnResult.columnId, columnResult.totalCount);
      typeCounts.put(columnResult.columnId, columnResult.typeCounts);
      nullCounts.put(columnResult.columnId, columnResult.nullCount);
      bestFits.put(columnResult.columnId, columnResult.bestFit);
      confidences.put(columnResult.columnId, columnResult.confidences);
    }
    
    return new Result(
      columnAnalyzers.keySet(),
      amountOfValues,
      nullCounts,
      typeCounts,
      confidences,
      bestFits,
      nullable,
      columnNullable,
      tolerance,
      columnTolerance);
  }
  
  private void analyzeValue(final String key, @Nullable final Object value, final Map<String, ColumnAnalyzer> columnAnalyzers) {
    LOGGER.debug("Analyzing key: {}, value: {}", key, value);
    
    columnAnalyzers.computeIfAbsent(key, k -> {
      final Boolean localNullable = this.columnNullable.getOrDefault(key, this.nullable);
      final Double localTolerance = this.columnTolerance.getOrDefault(key, this.tolerance);
      
      final Collection<List<Class>> localHierarchies = this.columnHierarchies.getOrDefault(key, new LinkedList<>());
      final Map<Class, DataTypeScanner> localScanners = this.columnScanners.getOrDefault(key, new HashMap<>());
      
      localHierarchies.addAll(this.hierarchies);
      localScanners.putAll(this.scanners);
      return new ColumnAnalyzer(key, localHierarchies, localScanners, localTolerance, localNullable);
    }).analyze(value);
  }
}
