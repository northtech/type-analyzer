package dk.northtech.datawash.Analyzer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@ParametersAreNonnullByDefault
class ColumnAnalyzerContainer<T extends ColumnAnalyzerInterface> {
  private static final Logger LOGGER = LoggerFactory.getLogger(ColumnAnalyzerContainer.class);
  
  private Map<String, T> columnAnalyzers = new HashMap<>();
  
  
  T get(String columnId) throws NoSuchElementException {
    if (!columnAnalyzers.containsKey(columnId)) {
      throw new NoSuchElementException();
    }
    return columnAnalyzers.get(columnId);
  }
  
  synchronized T addAndGet(String columnId, T columnAnalyzer) {
    if (columnAnalyzers.containsKey(columnId)) {
      return columnAnalyzers.get(columnId);
    }
    columnAnalyzers.put(columnId, columnAnalyzer);
    return columnAnalyzer;
  }
  
  Collection<T> values() {
    return columnAnalyzers.values();
  }
  
  Set<String> getColumnIds() {
    return columnAnalyzers.keySet();
  }
}
