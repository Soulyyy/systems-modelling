package validation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Hans on 29/10/2015.
 */
public class PetriNet {

  public


  List<TransitionObject> transitions;
  List<PlaceObject> places;
  Trace[] traces;

  int placeCount;

  public PlaceObject startingPlace;
  public PlaceObject endingPlace;

  public void mapStuff(EventLog eventLog) {
    List<Case> cases = eventLog.getCases();
    for (Case caseObj : cases) {
      Trace trace = caseObj.getTrace();
      List<Event> events = trace.getEvents();
      for (Event event : events) {
        for (TransitionObject transition : transitions) {
          if (event.transition == null && event.name != null && event.name.equals(transition.name)) {
            System.out.println("MA PEAN VIST PRINTIMA");
            System.out.println("MIDA VITTU SEE ASI TEEB?");
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

  public Trace iterateTrace(Trace trace) {
    if (this.startingPlace == null) {
      System.out.println("No starting place set, cannot execute");
      System.exit(1);
    }
    int tokenId = this.startingPlace.token != null ? this.startingPlace.token.id + 1 : 0;
    this.startingPlace.token = new Token(tokenId);

    List<Event> events = trace.events;
    //We initialize with starting place
    //PlaceObject cur = this.startingPlace;
    List<PlaceObject> placeObjects = new ArrayList<>();
    placeObjects.add(this.startingPlace);
    for (Event event : events) {
      String name = event.name;
      boolean found = false;
      for (PlaceObject cur : placeObjects) {
        boolean doubleBreak = false;
        for (TransitionObject transition : cur.outTransitions) {
          //want name check first, easier to do
          if (transition.name.equals(event.name) && transition.inPlaces.stream().allMatch(i -> i.token.isActivated())) {
            found = true;
            //consume all input tokens
            transition.inPlaces.stream().forEach(i -> i.token.consumeToken());
            //TODO does this work?
            trace.producedTokens+=transition.inPlaces.size();
            cur.token.consumeToken();
            transition.outPlaces.stream().forEach(i -> i.token = new Token(tokenId));
            //Clean up current places
            placeObjects = placeObjects.stream().filter(PlaceObject::canFire).collect(Collectors.toList());
            //Can collect fired nodes before
            placeObjects.addAll(transition.outPlaces);
            //hack to break out of 2 loops
            doubleBreak = true;
            break;
          }
        }
        if (doubleBreak) {
          break;
        }

      }
      if (found) {
        trace.consumedTokens++;
      } else {
        trace.missingTokens++;
      }
      trace.remainingTokens = this.placeCount - trace.producedTokens;
    }
    return trace;
  }

  @Override
  public String toString() {
    String resp = "";
<<<<<<< HEAD
=======
    return resp;
>>>>>>> a3e11d3e38e773b0c1a4d529bd144b813987c1d1
  }

}