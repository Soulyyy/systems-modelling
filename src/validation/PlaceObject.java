package validation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hans on 29/10/2015.
 */
public class PlaceObject {

  public int tokenCount;
  private String name;

  public Token token;

  //These lists represent directed edges
  public List<TransitionObject> inTransitions;
  public List<TransitionObject> outTransitions;

  public PlaceObject(String name) {
    this.name = name;
    this.inTransitions = new ArrayList<>();
    this.outTransitions = new ArrayList<>();
    this.tokenCount = 0;
  }

  public void addInTransition(TransitionObject transition) {
    this.inTransitions.add(transition);
  }

  public void addOutTransition(TransitionObject transitionObject) {
    this.outTransitions.add(transitionObject);
  }

  public boolean canFire() {
    return tokenCount >= 1;
  }

  public void addToken() {
    this.tokenCount++;
  }

  public void consumeToken() throws IllegalStateException {
    if (this.tokenCount <= 0) {
      throw new IllegalStateException("No tokens to use");
    }
    this.tokenCount--;
  }

  @Override
  public String toString() {
    return name;
  }
}
