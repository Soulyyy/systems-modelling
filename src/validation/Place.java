package validation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hans on 29/10/2015.
 */
public class Place {

  public int tokenCount;
  private String name;

  //These lists represent directed edges
  List<Transition> transitions;

  public Place(String name) {
    this.name = name;
    this.transitions = new ArrayList<>();
    this.tokenCount = 0;
  }

  public void addTransition(Transition transition) {
    this.transitions.add(transition);
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
}
