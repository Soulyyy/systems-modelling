package validation;

import java.util.List;

/**
 * Created by Hans on 29/10/2015.
 */
public class PetriNet {


  List<Transition> transitions;
  List<Place> places;
  Trace[] traces;

  public void mapStuff(EventLog eventLog) {
    Case[] cases = eventLog.getCases();
    for(Case caseObj : cases) {
      Trace trace = caseObj.getTrace();
      Event[] events = trace.getEvents();
      for(Event event : events) {
        for(Transition transition : transitions) {
          if(event.transition == null && event.name != null && event.name.equals(transition.name)) {

          }
        }
      }
    }
    setTransitions();
  }

  public void setTransitions() {

  }

  public LogResult iterate(Trace trace) {
    return new LogResult();
  }
}
