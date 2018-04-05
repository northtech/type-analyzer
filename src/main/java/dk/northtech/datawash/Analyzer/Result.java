package dk.northtech.datawash.Analyzer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A immutable result, holding all the information about the best fitting data types in the different columns, how many
 * values of each type, the total number of values, total number of nulls found, the specific settings that were used
 * to arrive at these results, and the confidence for all data types for each column.
 */
@ParametersAreNonnullByDefault
public class Result {
  private static final Logger LOGGER = LoggerFactory.getLogger(Result.class);
  
  private final Set<String>                      columns        = new HashSet<>();
  private final Map<String, Integer>             amountOfValues = new HashMap<>();
  private final Map<String, Map<Class, Integer>> typeCounts     = new HashMap<>();
  private final Map<String, Integer>             nullCounts     = new HashMap<>();
  private final Map<String, Map<Class, Double>>  confidences    = new HashMap<>();
  private final Map<String, Class>               bestFits       = new HashMap<>();
  
  private final Map<String, Boolean> columnNullable  = new HashMap<>();
  private final Map<String, Double>  columnTolerance = new HashMap<>();
  
  private final Double  tolerance;
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
  
  /**
   * Get all the columns that were found in the data set.
   *
   * @return the {@code Set} of all the columns that were found in the data set.
   */
  public Set<String> getColumns() {
    return new HashSet<>(columns);
  }
  
  /**
   * Get the total amount of values in a column.
   *
   * @param column the column.
   *
   * @return amount of values in the specified column.
   */
  public Integer getAmountOfValues(String column) {
    return amountOfValues.getOrDefault(column, 0);
  }
  
  /**
   * Get the amount of values that qualified to have a specific data type.
   *
   * @param column the column.
   * @param type   the type.
   *
   * @return the amount of values that had a specific type.
   */
  public Integer getAmountOfType(String column, Class type) {
    return typeCounts.get(column).getOrDefault(type, 0);
  }
  
  /**
   * Get the tolerance that was used to generate this {@code Result}.
   *
   * @return the tolerance.
   */
  public Double getTolerance() {
    return tolerance;
  }
  
  /**
   * Get the tolerance used on a specific column.
   *
   * @param columnId the column.
   *
   * @return the tolerance of the specified column.
   */
  public Double getTolerance(String columnId) {
    return columnTolerance.getOrDefault(columnId, this.tolerance);
  }
  
  /**
   * Get whether or not the data was allowed to be null.
   *
   * @return True if the data was nullable.
   */
  public Boolean getNullable() {
    return nullable;
  }
  
  /**
   * Get whether or not the data was allowed to be null for a specific column.
   *
   * @param columnId the column.
   *
   * @return The nullability of the specified column.
   */
  public Boolean getNullable(String columnId) {
    return columnNullable.getOrDefault(columnId, this.nullable);
  }
  
  /**
   * Get the proportion of values in a specific column that matched a specific type.
   *
   * @param columnId the column.
   * @param type     the type.
   *
   * @return the confidence.
   */
  public Double getConfidence(String columnId, Class type) {
    return confidences.get(columnId).get(type);
  }
  
  /**
   * Get the amount of {@code null} values in a specific column.
   *
   * @param columnId the column.
   *
   * @return the amount of nulls.
   */
  public Integer getNullCount(String columnId) {
    return nullCounts.get(columnId);
  }
  
  /**
   * Get the best fitting data type for a column.
   *
   * @param columnId the column.
   *
   * @return the best fitting data type.
   */
  public Class getBestFit(String columnId) {
    return bestFits.get(columnId);
  }
}
