package dk.northtech.datawash.stages;

import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import dk.northtech.datawash.DataTypeAnalyzer;
import dk.northtech.datawash.DataTypeAnalyzerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AnalyzerState<SELF extends AnalyzerState<SELF>> extends DataState<SELF> {
  private static final Logger LOGGER = LoggerFactory.getLogger(AnalyzerState.class);
  
  @ProvidedScenarioState
  DataTypeAnalyzer analyzer;
  
  private DataTypeAnalyzerBuilder builder = new DataTypeAnalyzerBuilder();
  
  public SELF the_analyzer_recognizes(Class... classHierarchyToScanFor) {
    // TODO: Configure the Analyzer/Builder.
    builder.addHierarchy(classHierarchyToScanFor);
    return self();
  }
  
  public SELF the_analyzer_is_instantiated() {
    // TODO: This should build the Analyzer object, possibly with some fluent builder for registered scanners
    this.analyzer = builder.build();
    return self();
  }
}
