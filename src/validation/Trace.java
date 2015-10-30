package validation;

import java.util.List;

/**
 * Created by Hans on 29/10/2015.
 */
public class Trace {

  private int totalTraces;
  private int missingTokens;
  private int remainingTokens;
  private int consumedTokens;
  private int producedTokens;

  private int id;

    List<Event> events;

  public Trace(int id, List<Event> events){
    this.id = id;
    this.events = events;
  }

  public List<Event> getEvents() {
    return events;
  }
}
