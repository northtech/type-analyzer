package dk.northtech.datawash.Analyzer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.stream.Collectors;

@ParametersAreNonnullByDefault
class ConcurrentColumnAnalyzer implements ColumnAnalyzerInterface {
  private static final Logger LOGGER = LoggerFactory.getLogger(ConcurrentColumnAnalyzer.class);
  
  private static final Integer CORES = Runtime.getRuntime().availableProcessors();
  
  static {
    LOGGER.info("Using {} core(s)", CORES);
  }
  
  private final String                  columnId;
  private final Double                  tolerance;
  private final Boolean                 nullable;
  private final Collection<List<Class>> hierarchies         = new LinkedList<>();
  private       List<ColumnAnalyzer>    columnAnalyzers     = new ArrayList<>();
  private       Integer                 columnAnalyzerIndex = 0;
  
  public ConcurrentColumnAnalyzer(String columnId,
                                  Collection<List<Class>> hierarchies,
                                  Map<Class, DataTypeScanner> scanners,
                                  Double tolerance,
                                  Boolean nullable) {
    this.hierarchies.addAll(hierarchies);
    this.columnId = columnId;
    this.tolerance = tolerance;
    this.nullable = nullable;
    
    for (int i = 0; i < CORES; i++) {
      columnAnalyzers.add(new ColumnAnalyzer(columnId, hierarchies, scanners, tolerance, nullable));
    }
  }
  
  @Override
  public void analyze(Object value) {
    columnAnalyzers.get(columnAnalyzerIndex % CORES).analyze(value);
    columnAnalyzerIndex++;
    if (columnAnalyzerIndex >= 4) {
      columnAnalyzerIndex = 0;
    }
  }
  
  @Override
  public ColumnResult getResult() {
    List<ColumnResult> columnResults =
      columnAnalyzers.stream().map(ColumnAnalyzer::getResult).collect(Collectors.toList());
    
    final Map<Class, Integer> totalTypeCounts = new HashMap<>();
    Integer                   totalTotalCount = 0;
    Integer                   totalNullCount  = 0;
    
    for (ColumnResult columnResult : columnResults) {
      Integer                   totalCount = columnResult.totalCount;
      Integer                   nullCount  = columnResult.nullCount;
      final Map<Class, Integer> typeCounts = columnResult.typeCounts;
      
      totalTotalCount += totalCount;
      totalNullCount += nullCount;
      
      typeCounts.keySet().forEach(k1 -> totalTypeCounts.computeIfPresent(k1, (k2, v) -> v + typeCounts.get(k2)));
      typeCounts.keySet().forEach(k1 -> totalTypeCounts.putIfAbsent(k1, typeCounts.get(k1)));
    }
    
    // TODO: calculate confidences and best fit
    Map<Class, Double> confidences = calculateConfidences(totalTypeCounts, totalTotalCount);
    Class              bestFit     = getBestFit(confidences);
    
    return new ColumnResult(columnId,
                            totalTotalCount,
                            totalNullCount,
                            totalTypeCounts,
                            confidences,
                            bestFit,
                            tolerance,
                            nullable);
  }
  
  private Map<Class, Double> calculateConfidences(Map<Class, Integer> typeCounts, Integer totalCount) {
    final Map<Class, Double> confidences = new HashMap<>();
    for (List<Class> hierarchy : hierarchies) {
      for (Class type : hierarchy) {
        Integer count = typeCounts.get(type);
        confidences.put(type, (double) count / (double) totalCount);
      }
    }
    return confidences;
  }
  
  private Class getBestFit(final Map<Class, Double> confidences) {
    Class  bestFit        = null;
    Double bestConfidence = 0.0;
    
    for (List<Class> hierarchy : hierarchies) {
      for (Class type : hierarchy) {
        Double confidence = confidences.get(type);
        
        if (confidence >= (1.0 - tolerance) && confidence > bestConfidence) {
          bestConfidence = confidence;
          bestFit = type;
          break;
        }
      }
    }
    
    return bestFit;
  }
}
