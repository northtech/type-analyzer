package dk.northtech.datawash;

import dk.northtech.datawash.DataTypeScanning.DataTypeScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@ParametersAreNonnullByDefault
class ColumnAnalyzer {
  private static final Logger LOGGER = LoggerFactory.getLogger(ColumnAnalyzer.class);
  
  private final String columndId;
  private final Double tolerance;
  private final Boolean nullable;
  
  private final Map<Class, DataTypeScanner> scanners = new HashMap<>();
  private final Collection<List<Class>> hierarchies = new LinkedList<>();
  private final Map<Class, Integer> typeCounts = new HashMap<>();
  
  
  private Integer totalCount = 0;
  private Integer nullCount = 0;
  
  
  ColumnAnalyzer(String columnId, Collection<List<Class>> hierarchies, Map<Class, DataTypeScanner> scanners, Double tolerance, Boolean nullable) {
    this.hierarchies.addAll(hierarchies);
    this.scanners.putAll(scanners);
    this.columndId = columnId;
    this.tolerance = tolerance;
    this.nullable = nullable;
  }
  
  ColumnResult getResult() {
    Class bestFit = null;
    Double bestConfidence = 0.0;
    final Map<Class, Double> confidences = new HashMap<>();
    
    for (List<Class> hierarchy : hierarchies) {
      for (Class type : hierarchy) {
        Integer count = typeCounts.getOrDefault(type, 0);
        confidences.put(type, (double) count / (double) totalCount);
      }
    }
    
    for (List<Class> hierarchy : hierarchies) {
      for (Class type : hierarchy) {
        Double confidence = confidences.get(type);
        
        if (confidence >= (1.0 - tolerance) && confidence > bestConfidence) {
          bestConfidence = confidence;
          bestFit = type;
          break; // this is so we don't override a bestFit choice with a type higher in the hierarchy
        }
      }
    }
    
    if (bestFit == null) {
      LOGGER.warn("A best fit was not found for column: {}, with tolerance: {}, and nullable: {}", columndId, tolerance, nullable);
    }
    
    return new ColumnResult(columndId, totalCount, nullCount, typeCounts, confidences, bestFit, tolerance, nullable);
  }
  
  void analyze(@Nullable final Object value) {
    totalCount++;
    
    if (value == null) {
      nullCount++;
      
      if (nullable) {
        hierarchies.forEach(hierarchy -> hierarchy.forEach(this::typeCountIncrement));
      }
      
      return;
    }
    
    final Map<Class, Boolean> memoizedScan = new HashMap<>();
    
    for (List<Class> hierarchy : hierarchies) {
      boolean foundMatch = false;
      for (Class type : hierarchy) {
        if (foundMatch || memoizedScan.computeIfAbsent(type, k -> scanners.get(k).scan(value))) {
          foundMatch = true;
          typeCountIncrement(type);
        }
      }
    }
  }
  
  private void typeCountIncrement(Class type) {
    typeCounts.computeIfPresent(type, (k, v) -> v + 1);
    typeCounts.putIfAbsent(type, 1);
  }
}
