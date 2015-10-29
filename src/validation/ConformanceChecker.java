package validation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hans on 29/10/2015.
 */
public class ConformanceChecker {
  static Trace[] traces;
  static PetriNet petriNet;

  public static void main(String[] args) {
    PetriNet petriNet = new PetriNet();
    EventLog eventLog = new EventLog();
    petriNet.mapStuff(eventLog);
    traces = Arrays.stream(eventLog.getCases()).map(Case::getTrace).toArray(Trace[]::new);

    recursiveMethod();


  }

  public static LogResult recursiveMethod() {
    Map<Trace, LogResult> traceLogResultMap = new HashMap<>();
    for (Trace trace : traces) {
      //Tell me what comes back
      LogResult logResult = petriNet.iterate(trace);
      traceLogResultMap.put(trace, logResult);
    }
    if(true) {
      //OR map?
      return new LogResult();
    }
    recursiveMethod();
    return new LogResult();
  }

  public static String calculateConformance() {
    return "TERE";
  }
}
