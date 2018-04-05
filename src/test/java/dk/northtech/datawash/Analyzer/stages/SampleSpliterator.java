package dk.northtech.datawash.Analyzer.stages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;
import java.util.Spliterator;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
class SampleSpliterator implements Spliterator<Map<String, Object>> {
  private static final Logger              LOGGER = LoggerFactory.getLogger(SampleSpliterator.class);
  final                Map<String, Object> templateRow;
  Long remainingElements;
  
  SampleSpliterator() {
    templateRow = new HashMap<>();
  }
  
  private SampleSpliterator(Long remainingElements, Map<String, Object> templateRow) {
    this.remainingElements = remainingElements;
    this.templateRow = templateRow;
  }
  
  @Override
  public boolean tryAdvance(Consumer<? super Map<String, Object>> action) {
    if (remainingElements > 0) {
      
      Map<String, Object> row = new HashMap<>();
      row.putAll(templateRow);
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
        remainingElements /= 2;
        return new SampleSpliterator(remainingElements, templateRow);
      }
      else {
        remainingElements /= 2;
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
