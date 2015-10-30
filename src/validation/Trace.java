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

  private int id;

  List<Event> events;

  public Trace(int id, List<Event> events) {
    this.id = id;
    this.events = events;
  }

  public List<Event> getEvents() {
    return events;
  }

  @Override
  public String toString() {
    return "Trace{" +
        "totalTraces=" + totalTraces +
        ", missingTokens=" + missingTokens +
        ", remainingTokens=" + remainingTokens +
        ", consumedTokens=" + consumedTokens +
        ", producedTokens=" + producedTokens +
        ", id=" + id +
        "[" + events.stream().map(Object::toString).reduce((j, h) -> j + " " + h) + "]" +
        '}';
  }

}
