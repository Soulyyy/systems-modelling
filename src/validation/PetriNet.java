package validation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Hans on 29/10/2015.
 */
public class PetriNet {

  List<TransitionObject> transitions;

  int placeCount;
  public int transitionCount;
  public int labelCount;

  public PlaceObject startingPlace;
  public PlaceObject endingPlace;

  public Trace iterateTrace(Trace trace) {
    if (this.startingPlace == null) {
      System.out.println("No starting place set, cannot execute");
      System.exit(1);
    }
    int tokenId = this.startingPlace.token != null ? this.startingPlace.token.id + 1 : 0;
    this.startingPlace.token = new Token(tokenId);

    List<Token> tokens = new LinkedList<>();
    List<Event> events = trace.events;
    //We initialize with starting place
    List<PlaceObject> placeObjects = new ArrayList<>();
    placeObjects.add(this.startingPlace);
    for (Event event : events) {
      boolean found = false;
      for (PlaceObject cur : placeObjects) {
        boolean doubleBreak = false;
        for (TransitionObject transition : cur.outTransitions) {
          //want name check first, easier to do
          if (transition.name.equals(event.name) && transition.inPlaces.stream().allMatch(i -> i.token.isActivated())) {
            trace.numberFirings += cur.outTransitions.size();
            trace.numberIterations++;
            found = true;
            //consume all input tokens
            transition.inPlaces.stream().forEach(i -> i.token.consumeToken());
            trace.producedTokens += transition.inPlaces.size();
            cur.token.consumeToken();
            Token token = new Token(tokenId);
            tokens.add(token);
            transition.outPlaces.stream().forEach(i -> i.token = token);
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
    }
    if (this.endingPlace.canFire()) {
      trace.producedTokens++;
    }
    endingPlace.token.consumeToken();
    trace.remainingTokens = (int) tokens.stream().filter(Token::isActivated).count();
    //trace.remainingTokens = this.placeCount - trace.producedTokens;
    return trace;
  }

}