package validation;

import java.util.Map;

/**
 * Created by Hans on 29/10/2015.
 */
public class Case {
  private Trace trace;
  private Map<String, String> attributes;
  private int id;

  public Case(int id, Trace trace, Map<String,String> attributes){
      this.id = id;
      this.trace = trace;
      this.attributes = attributes;
  }

  public Trace getTrace() {
    return trace;
  }

  @Override
  public String toString() {
    return "Case {"+
        "id="+id+
        ", trace=" +trace +
        ", attributes="+attributes+
        "}";
  }

}
