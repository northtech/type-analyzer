package dk.northtech.datawash.Analyzer;

import dk.northtech.datawash.Analyzer.DataTypeScanning.DataTypeScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@ParametersAreNonnullByDefault
public class DataTypeAnalyzer {
  private static final Logger LOGGER = LoggerFactory.getLogger(DataTypeAnalyzer.class);
  
  private final Collection<List<Class>>     hierarchies = new LinkedList<>();
  private final Map<Class, DataTypeScanner> scanners    = new HashMap<>();
  
  private final Map<String, Collection<List<Class>>>     columnHierarchies = new HashMap<>();
  private final Map<String, Map<Class, DataTypeScanner>> columnScanners    = new HashMap<>();
  private final Map<String, Double>                      columnTolerance   = new HashMap<>();
  private final Map<String, Boolean>                     columnNullable    = new HashMap<>();
  
  private Double  tolerance;
  private Boolean nullable;
  
  DataTypeAnalyzer(Collection<List<Class>> hierarchies, Map<String, Collection<List<Class>>> columnHierarchies,
                   Map<Class, DataTypeScanner> scanners, Map<String, Map<Class, DataTypeScanner>> columnScanners,
                   Double tolerance, Map<String, Double> columnTolerance, Boolean nullable,
                   Map<String, Boolean> columnNullable) {
    
    this.hierarchies.addAll(hierarchies);
    this.scanners.putAll(scanners);
    this.tolerance = tolerance;
    this.nullable = nullable;
    
    this.columnHierarchies.putAll(columnHierarchies);
    this.columnScanners.putAll(columnScanners);
    this.columnTolerance.putAll(columnTolerance);
    this.columnNullable.putAll(columnNullable);
    
  }
  
  /**
   * @param dataCollection A Collection of Maps from String to Object, each Map represents a row in the data
   *
   * @return immutable result object
   */
  public Result analyze(final Collection<Map<String, Object>> dataCollection) {
    return analyze(dataCollection.stream());
  }
  
  /**
   * @param dataStream a Stream of Maps from String to Object, each Map represents a row in the data
   *
   * @return immutable result object
   */
  public Result analyze(final Stream<Map<String, Object>> dataStream) {
    //    final Boolean USE_NEW = false;
    
    final Map<String, ColumnAnalyzer> columnAnalyzers = new ConcurrentHashMap<>();
    //    final ColumnAnalyzerContainer<ConcurrentColumnAnalyzer> columnAnalyzerContainer = new
    // ColumnAnalyzerContainer<>();
    
    //    if (USE_NEW) {
    //      dataStream.parallel().forEach(row -> analyzeRow(row, columnAnalyzerContainer));
    //    }
    //    else {
    dataStream.parallel().forEach(row -> analyzeRow(row, columnAnalyzers));
    //    }
    
    final Map<String, Integer>             amountOfValues = new HashMap<>();
    final Map<String, Map<Class, Integer>> typeCounts     = new HashMap<>();
    final Map<String, Integer>             nullCounts     = new HashMap<>();
    final Map<String, Class>               bestFits       = new HashMap<>();
    final Map<String, Map<Class, Double>>  confidences    = new HashMap<>();
    
    //    if (USE_NEW) {
    //      columnAnalyzerContainer.values().stream().map(ColumnAnalyzerInterface::getResult).forEach(columnResult -> {
    //        amountOfValues.put(columnResult.columnId, columnResult.totalCount);
    //        typeCounts.put(columnResult.columnId, columnResult.typeCounts);
    //        nullCounts.put(columnResult.columnId, columnResult.nullCount);
    //        bestFits.put(columnResult.columnId, columnResult.bestFit);
    //        confidences.put(columnResult.columnId, columnResult.confidences);
    //      });
    //
    //      return new Result(
    //        columnAnalyzerContainer.getColumnIds(),
    //        amountOfValues,
    //        nullCounts,
    //        typeCounts,
    //        confidences,
    //        bestFits,
    //        nullable,
    //        columnNullable,
    //        tolerance,
    //        columnTolerance);
    //    }
    //    else {
    columnAnalyzers.values().stream().map(ColumnAnalyzerInterface::getResult).forEach(columnResult -> {
      amountOfValues.put(columnResult.columnId, columnResult.totalCount);
      typeCounts.put(columnResult.columnId, columnResult.typeCounts);
      nullCounts.put(columnResult.columnId, columnResult.nullCount);
      bestFits.put(columnResult.columnId, columnResult.bestFit);
      confidences.put(columnResult.columnId, columnResult.confidences);
    });
    
    return new Result(columnAnalyzers.keySet(), amountOfValues, nullCounts, typeCounts, confidences, bestFits, nullable,
                      columnNullable, tolerance, columnTolerance);
    //    }
  }
  
  //  private void analyzeRow(final Map<String, Object> row, final ColumnAnalyzerContainer<ConcurrentColumnAnalyzer>
  //    columnAnalyzerContainer) {
  //    row.keySet().forEach(k -> analyzeValue(k, row.get(k), columnAnalyzerContainer));
  //  }
  
  private void analyzeRow(final Map<String, Object> row, final Map<String, ColumnAnalyzer> columnAnalyzers) {
    row.keySet().forEach(k -> analyzeValue(k, row.get(k), columnAnalyzers));
  }
  
  //  private void analyzeValue(final String columnId, @Nullable final Object value, final ColumnAnalyzerContainer
  //    columnAnalyzerContainer) {
  //    LOGGER.debug("Analyzing key: {}, value: {}", columnId, value);
  //
  //    try {
  //      columnAnalyzerContainer.get(columnId).analyze(value);
  //    }
  //    catch (NoSuchElementException e) {
  //      columnAnalyzerContainer.addAndGet(columnId, this.createConcurrentColumnAnalyzer(columnId)).analyze(value);
  //    }
  //  }
  
  private void analyzeValue(final String columnId, @Nullable final Object value,
                            final Map<String, ColumnAnalyzer> columnAnalyzers) {
    LOGGER.debug("Analyzing key: {}, value: {}", columnId, value);
    
    columnAnalyzers.computeIfAbsent(columnId, this::createColumnAnalyzer).analyze(value);
  }
  
  //  private ConcurrentColumnAnalyzer createConcurrentColumnAnalyzer(final String columnId) {
  //    LOGGER.debug("Creating ConcurrentColumnAnalyzer for column: {}", columnId);
  //
  //    final Boolean localNullable = this.columnNullable.getOrDefault(columnId, this.nullable);
  //    final Double localTolerance = this.columnTolerance.getOrDefault(columnId, this.tolerance);
  //
  //    final Collection<List<Class>> localHierarchies = this.columnHierarchies.getOrDefault(columnId, new LinkedList<>
  // ());
  //    final Map<Class, DataTypeScanner> localScanners = this.columnScanners.getOrDefault(columnId, new HashMap<>());
  //
  //    localHierarchies.addAll(this.hierarchies);
  //    localScanners.putAll(this.scanners);
  //
  //    return new ConcurrentColumnAnalyzer(columnId, localHierarchies, localScanners, localTolerance, localNullable);
  //  }
  
  private ColumnAnalyzer createColumnAnalyzer(final String columnId) {
    LOGGER.debug("Creating ColumnAnalyzer for column: {}", columnId);
    
    final Boolean localNullable  = this.columnNullable.getOrDefault(columnId, this.nullable);
    final Double  localTolerance = this.columnTolerance.getOrDefault(columnId, this.tolerance);
    
    final Collection<List<Class>> localHierarchies = this.columnHierarchies.getOrDefault(columnId, new LinkedList<>());
    final Map<Class, DataTypeScanner> localScanners = this.columnScanners.getOrDefault(columnId, new HashMap<>());
    
    localHierarchies.addAll(this.hierarchies);
    localScanners.putAll(this.scanners);
    
    return new ColumnAnalyzer(columnId, localHierarchies, localScanners, localTolerance, localNullable);
  }
}
