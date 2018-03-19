package dk.northtech.datawash;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@ParametersAreNonnullByDefault
public class Result {
  private static final Logger LOGGER = LoggerFactory.getLogger(Result.class);
  
  private final Set<String> columns = new HashSet<>();
  private final Map<String, Integer> amountOfValues = new HashMap<>();
  private final Map<String, Map<Class, Integer>> typeCounts = new HashMap<>();
  private final Map<String, Integer> nullCounts = new HashMap<>();
  private final Map<String, Map<Class, Double>> confidences = new HashMap<>();
  private final Map<String, Class> bestFits = new HashMap<>();
  
  private final Map<String, Boolean> columnNullable = new HashMap<>();
  private final Map<String, Double> columnTolerance = new HashMap<>();
  
  private final Double tolerance;
  private final Boolean nullable;
  
  
  Result(Set<String> columns,
         Map<String, Integer> amountOfValues,
         Map<String, Integer> nullCounts,
         Map<String, Map<Class, Integer>> typeCounts,
         Map<String, Map<Class, Double>> confidences,
         Map<String, Class> bestFits,
         Boolean nullable,
         Map<String, Boolean> columnNullable,
         Double tolerance,
         Map<String, Double> columnTolerance) {
    
    this.columns.addAll(columns);
    this.amountOfValues.putAll(amountOfValues);
    this.typeCounts.putAll(typeCounts);
    this.nullCounts.putAll(nullCounts);
    this.confidences.putAll(confidences);
    this.bestFits.putAll(bestFits);
    
    this.columnNullable.putAll(columnNullable);
    this.columnTolerance.putAll(columnTolerance);
    
    this.nullable = nullable;
    this.tolerance = tolerance;
  }
  
  public HashSet<String> getColumns() {
    return new HashSet<>(columns);
  }
  
  public Integer getAmountOfValues(String column) {
    return amountOfValues.get(column);
  }
  
  public Integer getAmountOfType(String column, Class type) {
    return typeCounts.get(column).get(type);
  }
  
  public Double getTolerance() {
    return tolerance;
  }
  
  public Double getTolerance(String columnId) {
    return columnTolerance.getOrDefault(columnId, this.tolerance);
  }
  
  public Boolean getNullable() {
    return nullable;
  }
  
  public Boolean getNullable(String columnId) {
    return columnNullable.getOrDefault(columnId, this.nullable);
  }
  
  public Double getConfidence(String columnId, Class type) {
    return confidences.get(columnId).get(type);
  }
  
  public Integer getNullCount(String columnId) {
    return nullCounts.get(columnId);
  }
  
  public Class getBestFit(String columnId) {
    return bestFits.get(columnId);
  }
}
