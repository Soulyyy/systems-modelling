package validation;

import java.util.Map;

/**
 * Created by Hans on 29/10/2015.
 */
public class Case {
  private Trace trace;
  private Map<String, String> attributes;

  public Case(Trace trace, Map<String,String> attributes){
      this.trace = trace;
      this.attributes = attributes;
  }

  public Trace getTrace() {
    return trace;
  }
}
