package dk.northtech.typeanalyzer.Analyzer.stages;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import dk.northtech.typeanalyzer.Analyzer.DataTypeAnalyzer;
import dk.northtech.typeanalyzer.Analyzer.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@ParametersAreNonnullByDefault
public class AnalyzerActions<SELF extends AnalyzerActions<SELF>> extends Stage<SELF> {
  private static final Logger LOGGER = LoggerFactory.getLogger(AnalyzerActions.class);

  @ExpectedScenarioState
  List<Map<String, Object>> dataList;

  @ExpectedScenarioState
  Stream<Map<String, Object>> dataStream;

  @ExpectedScenarioState
  DataTypeAnalyzer analyzer;

  @ProvidedScenarioState
  Result result;

  public SELF the_list_is_analyzed() {
    result = analyzer.analyze(dataList);
    return self();
  }

  public SELF the_stream_is_analyzed() {
    result = analyzer.analyze(dataStream);
    return self();
  }
}
