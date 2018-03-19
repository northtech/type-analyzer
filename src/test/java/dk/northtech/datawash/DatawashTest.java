package dk.northtech.datawash;

import com.tngtech.jgiven.junit5.ScenarioTest;
import dk.northtech.datawash.stages.AnalyzerActions;
import dk.northtech.datawash.stages.AnalyzerOutcome;
import dk.northtech.datawash.stages.AnalyzerState;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.time.Instant;

public class DatawashTest extends ScenarioTest<AnalyzerState<?>, AnalyzerActions<?>, AnalyzerOutcome<?>> {
  private static final Logger LOGGER = LoggerFactory.getLogger(DatawashTest.class);
  
  @Test
  void aggregatesColumns() {
    given()
      .a_memory_list_of_Map_objects()
      .and().the_list_contains_object("columnA", 1, "columnB", "Alpha")
      .and().the_list_contains_object("columnA", 2, "columnC", 2.2)
      
      .and().the_analyzer_recognizes(Integer.class, Double.class, String.class)
      .and().the_analyzer_is_instantiated();
    when()
      .the_list_is_analyzed();
    then()
      .the_result_contains_columns("columnA", "columnB", "columnC");
  }
  
  @Test
  void recognizesDataTypeInColumn() {
    // We want a plugin structure where we can register scanners for data types,
    // so we can add more complex data types later (say, recognizing WKT strings or GeoJSON object structures).
    // This also lets us control which types we're actively scanning for, so we don't do expensive scanning
    // for types we're not going to use anyway.
    given()
      .a_memory_list_of_Map_objects()
      .and().the_list_contains_object("col", 1)
      .and().the_list_contains_object("col", 2)
      .and().the_list_contains_object("col", 3.0)
      .and().the_list_contains_object("col", 4.4)
      
      .and().the_analyzer_recognizes(Integer.class, Double.class, String.class)
      .and().the_analyzer_is_instantiated();
    when()
      .the_list_is_analyzed();
    then()
      .the_number_of_values_in_column_$_which_are_assignable_to_class_$_is_$("col", Integer.class, 3);
  }
  
  @Test
  void findsBestDataType() {
    given()
      .a_memory_list_of_Map_objects()
      .and().the_list_contains_object("col", 1)
      .and().the_list_contains_object("col", 2)
      .and().the_list_contains_object("col", 3.0)
      
      .and().the_analyzer_recognizes(Integer.class, Double.class)
      .and().the_analyzer_is_instantiated();
    when()
      .the_list_is_analyzed();
    then()
      .the_best_type_for_column_$_is_$("col", Integer.class);
  }
  
  @Test
  void recognizesIsoDates() {
    // I have included this as an example of a more "advanced" data type than the built-in primitives,
    // to highlight how we need to be able to plug in scanners for arbitrary types.
    // Talk to me before writing the actual string analysis for recognizing the ISO format; I have most
    // of it already.
    given()
      .a_memory_list_of_Map_objects()
      .and().the_list_contains_object("col", "2018-03-12")
      .and().the_list_contains_object("col", "2018-03-12T20:36:39.200Z")
      .and().the_list_contains_object("col", "2018-03-12T21:36:39.200+0100")
      .and().the_list_contains_object("col", "Supercalifragilisticexpialidocious")
      .and().the_list_contains_object("col", null)
      
      .and().the_analyzer_recognizes(Instant.class, String.class)
      .and().the_analyzer_is_instantiated();
    when()
      .the_list_is_analyzed();
    then()
      .the_number_of_values_in_column_$_which_are_assignable_to_class_$_is_$("col", Instant.class, 3)
      // Arguably, it is not good style to test for two things at the same time, but on the other hand,
      // we want to test the code with null-values sprinkled in, so we might as well do the check when we have them anyway:
      .and().the_number_of_null_values_in_column_$_is_$("col", 1);
  }
  
  Pair pair(String key, @Nullable Object value) {
    return new Pair(key, value);
  }
  
}
