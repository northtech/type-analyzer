package dk.northtech.datawash.stages;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import dk.northtech.datawash.DataTypeAnalyzer;
import dk.northtech.datawash.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;

@ParametersAreNonnullByDefault
public class AnalyzerActions<SELF extends AnalyzerActions<SELF>> extends Stage<SELF> {
  private static final Logger LOGGER = LoggerFactory.getLogger(AnalyzerActions.class);
  
  @ExpectedScenarioState
  List<Map<String, Object>> dataList;
  
  @ExpectedScenarioState
  DataTypeAnalyzer analyzer;
  
  @ProvidedScenarioState
  Result result;
  
  public SELF the_list_is_analyzed() {
    // TODO: This should do something with the data and analyzer, and prepare a result object the Outcome stage can inspect.
    result = analyzer.analyze(dataList);
    return self();
  }
}
