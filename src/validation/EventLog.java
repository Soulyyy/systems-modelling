package validation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

  public List<Trace> getTraces() {
    Set<Trace> traceSet = new HashSet<>();
    cases.stream().forEach(i -> traceSet.add(i.getTrace()));
    return traceSet.stream().collect(Collectors.toList());

  }
}
