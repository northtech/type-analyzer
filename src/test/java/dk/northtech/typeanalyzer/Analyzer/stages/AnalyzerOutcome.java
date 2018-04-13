package dk.northtech.typeanalyzer.Analyzer.stages;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import dk.northtech.typeanalyzer.Analyzer.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Set;

import static com.google.common.truth.Truth.assertThat;

@ParametersAreNonnullByDefault
public class AnalyzerOutcome<SELF extends AnalyzerOutcome<SELF>> extends Stage<SELF> {
  private static final Logger LOGGER = LoggerFactory.getLogger(AnalyzerOutcome.class);

  @ExpectedScenarioState
  Result result;

  public SELF the_result_contains_columns(String... columns) {
    Set<String> columnsInResult = result.getColumns();
    assertThat(columnsInResult).containsExactlyElementsIn(columns);
    return self();
  }

  public SELF the_number_of_values_in_column_$_which_are_assignable_to_class_$_is_$(String columnName,
                                                                                    Class wantedClass,
                                                                                    int number) {
    int count = result.getAmountOfType(columnName, wantedClass);
    assertThat(count).isEqualTo(number);
    return self();
  }

  public SELF the_number_of_null_values_in_column_$_is_$(String columnName, int numberOfNulls) {
    int nullCount = result.getNullCount(columnName);
    assertThat(nullCount).isEqualTo(numberOfNulls);
    return self();
  }

  public SELF the_best_type_for_column_$_is_$(String columName, Class bestClass) {
    Class bestFound = result.getBestFit(columName);
    assertThat(bestFound).isEqualTo(bestClass);
    return self();
  }
}
