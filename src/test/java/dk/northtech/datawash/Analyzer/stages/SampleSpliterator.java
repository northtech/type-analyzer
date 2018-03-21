package dk.northtech.datawash.Analyzer.stages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Spliterator;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
public class SampleSpliterator implements Spliterator<Map<String, Object>> {
  private static final Logger LOGGER = LoggerFactory.getLogger(SampleSpliterator.class);
  
  Long remainingElements;
  Map<String, Object> templateRow = new HashMap<>();
  
  SampleSpliterator() {  }
  
  private SampleSpliterator(Long remainingElements, Map<String, Object> templateRow) {
    this.remainingElements = remainingElements;
    this.templateRow = templateRow;
  }
  
  @Override
  public boolean tryAdvance(Consumer<? super Map<String, Object>> action) {
    if (remainingElements > 0) {
      
      Map<String, Object> row = new HashMap<>();
      templateRow.keySet().forEach(k -> row.put(k, templateRow.get(k)));
      action.accept(row);
      remainingElements--;
      return true;
    }
    
    return false;
  }
  
  @Override
  public Spliterator<Map<String, Object>> trySplit() {
    if (remainingElements >= 2) {
      if (remainingElements % 2 == 0) {
        this.remainingElements /= 2;
        return new SampleSpliterator(remainingElements, templateRow);
      }
      else {
        this.remainingElements /= 2;
        return new SampleSpliterator(remainingElements + 1, templateRow);
      }
    }
    
    return null;
  }
  
  @Override
  public long estimateSize() {
    return remainingElements;
  }
  
  @Override
  public int characteristics() {
    return IMMUTABLE | NONNULL | SIZED | SUBSIZED;
  }
}
