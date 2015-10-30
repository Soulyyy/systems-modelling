package validation;

import java.util.List;

/**
 * Created by Hans on 29/10/2015.
 */
public class Trace {

  public int totalTraces;
  public int missingTokens;
  public int remainingTokens;
  public int consumedTokens;
  public int producedTokens;

  List<Event> events;
  public Trace(List<Event> events){
    this.events = events;
  }

  public List<Event> getEvents() {
    return events;
  }

  @Override
  public String toString() {
    return "Trace {" +
        "events="+events+
        ", totalTraces=" + totalTraces +
        ", missingTokens=" + missingTokens +
        ", remainingTokens=" + remainingTokens +
        ", consumedTokens=" + consumedTokens +
        ", producedTokens=" + producedTokens +
        "[" + events.stream().map(Object::toString).reduce((j, h) -> j + " " + h) + "]" +
        '}';
  }
}
