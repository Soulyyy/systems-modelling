package validation;

import java.util.List;

/**
 * Created by Hans on 29/10/2015.
 */
public class EventLog {

  private List<Case> cases;

  public EventLog(List<Case> cases) {
      this.cases = cases;
  }


  public List<Case> getCases() {
    return cases;
  }
}
