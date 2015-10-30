package validation;

import java.util.List;

/**
 * Created by Hans on 29/10/2015.
 */
public class Trace {

  protected int totalTraces;
  protected int missingTokens;
  protected int remainingTokens;
  protected int consumedTokens;
  protected int producedTokens;

  protected int iterationsNumber;
  protected int firingsNumber;
 private List<Event> events;

  public Trace(List<Event> events) {
    this.events = events;
  }

  @Override
  public String toString() {
    return "Trace {" +
        "events=" + events +
        ", totalTraces=" + totalTraces +
        ", missingTokens=" + missingTokens +
        ", remainingTokens=" + remainingTokens +
        ", consumedTokens=" + consumedTokens +
        ", producedTokens=" + producedTokens +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Trace trace = (Trace) o;

    if (totalTraces != trace.totalTraces) return false;
    if (missingTokens != trace.missingTokens) return false;
    if (remainingTokens != trace.remainingTokens) return false;
    if (consumedTokens != trace.consumedTokens) return false;
    if (producedTokens != trace.producedTokens) return false;
    if (events != null ? !events.equals(trace.events) : trace.events != null) return false;

    return true;
  }

  public List<Event> getEvents() {
    return events;
  }

  @Override
  public int hashCode() {
    int result = totalTraces;
    result = 31 * result + missingTokens;
    result = 31 * result + remainingTokens;
    result = 31 * result + consumedTokens;
    result = 31 * result + producedTokens;
    result = 31 * result + (events != null ? events.hashCode() : 0);
    return result;
  }
}
