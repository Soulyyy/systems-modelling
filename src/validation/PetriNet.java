package validation;

import java.util.List;

/**
 * Created by Hans on 29/10/2015.
 */
public class PetriNet {

  public


  List<TransitionObject> transitions;
  List<PlaceObject> places;
  Trace[] traces;

  public PlaceObject startingPlace;
  public PlaceObject endingPlace;

  public void mapStuff(EventLog eventLog) {
    List<Case> cases = eventLog.getCases();
    for(Case caseObj : cases) {
      Trace trace = caseObj.getTrace();
      List<Event> events = trace.getEvents();
      for(Event event : events) {
        for(TransitionObject transition : transitions) {
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
