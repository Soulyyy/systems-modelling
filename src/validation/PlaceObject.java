package validation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hans on 29/10/2015.
 */
public class PlaceObject {

  private String name;

  public Token token;

  //These lists represent directed edges
  public List<TransitionObject> inTransitions;
  public List<TransitionObject> outTransitions;

  public PlaceObject(String name) {
    this.name = name;
    this.inTransitions = new ArrayList<>();
    this.outTransitions = new ArrayList<>();
  }

  public void addInTransition(TransitionObject transition) {
    this.inTransitions.add(transition);
  }

  public void addOutTransition(TransitionObject transitionObject) {
    this.outTransitions.add(transitionObject);
  }

  @Override
  public String toString() {
    return name;
  }
}
