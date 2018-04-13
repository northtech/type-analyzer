package dk.northtech.typeanalyzer.Converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@ParametersAreNonnullByDefault
public class DataTypeConverter {
  private static final Logger LOGGER = LoggerFactory.getLogger(DataTypeConverter.class);

  private final Map<String, Class> columnType = new HashMap<>();

  DataTypeConverter(Map<String, Class> columnType) {

  }

  public Collection<Map<String, Object>> convert(Collection<Map<String, Object>> dataCollection) {

    return null;
  }

  public Stream<Map<String, Object>> convert(Stream<Map<String, Object>> dataStream) {

    return null;
  }
}
