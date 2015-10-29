package validation;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.extension.std.XLifecycleExtension;
import org.deckfour.xes.extension.std.XTimeExtension;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Hans on 29/10/2015.
 */
public class ConformanceChecker {
  static Trace[] traces;
  static PetriNet petriNet;

  public static void main(String[] args) {
    PetriNet petriNet = new PetriNet();
    EventLog eventLog = getEventLog(args[1]);
    petriNet.mapStuff(eventLog);
    /*traces = Arrays.stream(eventLog.getCases()).map(Case::getTrace).collect(Collectors.toList());*/

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

  public static EventLog getEventLog(String inputFile) {
      Map<String, Trace> traces = new HashMap<>();
      List<Case> cases = new ArrayList<Case>();
      try{
          XLog log = XLogReader.openLog(inputFile);
            for (XTrace trace : log){
                String traceName = XConceptExtension.instance().extractName(trace);
                System.out.println(traceName);
                XAttributeMap caseAttributes = trace.getAttributes();
                System.out.println(caseAttributes.keySet());
                List<Event> events = new ArrayList<Event>();
                Map<String, String> caseAttrs = new HashMap<>();
                for(XEvent event : trace){
                    String activityName = XConceptExtension.instance().extractName(event);
                    Date timestamp = XTimeExtension.instance().extractTimestamp(event);
                    String eventType = XLifecycleExtension.instance().extractTransition(event);
                    XAttributeMap eventAttributes = event.getAttributes();
                    Map<String, String> eventAttrs = new HashMap<>();
                    for(String key :eventAttributes.keySet()){
                        eventAttrs.put(key, eventAttributes.get(key).toString());
                    }
                    for(String key :caseAttributes.keySet()){
                        caseAttrs.put(key, caseAttributes.get(key).toString());
                    }
                    events.add(new Event(activityName, timestamp, eventAttrs));
                }
                Trace ourTrace = new Trace(Integer.parseInt(traceName), events);
                cases.add(new Case(ourTrace, caseAttrs));
            }
      } catch (Exception e) {
          e.printStackTrace();
      }
    return new EventLog(cases);
  }
}


