package validation;

/**
 * Created by Hans on 29/10/2015.
 */
public class Trace {

  private int totalTraces;
  private int missingTokens;
  private int remainingTokens;
  private int consumedTokens;
  private int producedTokens;

  Event[] events;

  public Event[] getEvents() {
    return events;
  }
}
