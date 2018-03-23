package dk.northtech.datawash.Analyzer;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
interface ColumnAnalyzerInterface {
  
  void analyze(@Nullable Object value);
  
  ColumnResult getResult();
}
