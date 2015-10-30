package validation;

import java.util.*;
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
    Map<Trace, Integer> traceMap = new HashMap<>();
    cases.forEach(i -> traceMap.put(i.getTrace(), traceMap.get(i.getTrace())== null ? 1 : traceMap.get(i.getTrace())+1));
    traceMap.forEach((i,j) -> i.totalTraces = j);
    return traceMap.keySet().stream().collect(Collectors.toList());

  }
}
