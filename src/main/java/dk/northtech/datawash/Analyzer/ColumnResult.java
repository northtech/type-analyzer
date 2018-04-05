package dk.northtech.datawash.Analyzer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;

@ParametersAreNonnullByDefault
class ColumnResult {
  private static final Logger LOGGER = LoggerFactory.getLogger(ColumnResult.class);
  
  final String              columnId;
  final Integer             totalCount;
  final Integer             nullCount;
  final Map<Class, Integer> typeCounts  = new HashMap<>();
  final Map<Class, Double>  confidences = new HashMap<>();
  final Double              tolerance;
  final Boolean             nullable;
  final Class               bestFit;
  
  ColumnResult(String columnId,
               Integer totalCount,
               Integer nullCount,
               @Nonnull Map<Class, Integer> typeCounts,
               Map<Class, Double> confidences,
               @Nullable Class bestFit,
               Double tolerance,
               Boolean nullable) {
    
    this.columnId = columnId;
    this.totalCount = totalCount;
    this.nullCount = nullCount;
    typeCounts.keySet().forEach(k -> this.typeCounts.put(k, typeCounts.get(k)));
    this.confidences.putAll(confidences);
    this.tolerance = tolerance;
    this.nullable = nullable;
    this.bestFit = bestFit;
  }
}
