package dk.northtech.datawash.DataTypeScanning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ParametersAreNonnullByDefault
public class WordScanner extends DataTypeScanner<WordScanner.Word> {
  private static final Logger LOGGER = LoggerFactory.getLogger(WordScanner.class);
  private StringScanner stringScanner = null;
  
  
  @Override
  public boolean scan(Object value) {
    if (stringScanner == null) {
      stringScanner = new StringScanner();
    }
    if (stringScanner.scan(value)) {
      String valueAsString = stringScanner.convert(value);
      Pattern pattern = Pattern.compile("$\\w+^");
      Matcher matcher = pattern.matcher(valueAsString);
      
      return matcher.find();
    }
    
    return false;
  }
  
  @Override
  public Word convert(Object value) {
    if (stringScanner == null) {
      stringScanner = new StringScanner();
    }
    return new Word(stringScanner.convert(value));
  }
  
  @ParametersAreNonnullByDefault
  public static class Word {
    private static final Logger LOGGER = LoggerFactory.getLogger(Word.class);
    
    public final String word;
    
    public Word(String word) {
      this.word = word;
    }
  }
}
