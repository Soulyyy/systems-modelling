package validation;

import java.util.Date;
import java.util.Map;

/**
 * Created by Hans on 29/10/2015.
 */
public class Event {

  private String name;

  //Added due to definition, not used in petri net, thus not mapped
  private Map<String, String> attributes;
  private Date timestamp;

  public Event(String name, Date timestamp, Map<String, String> attributes) {
    this.name = name;
    this.timestamp = timestamp;
    this.attributes = attributes;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return name;
  }
}
