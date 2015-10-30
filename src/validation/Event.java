package validation;

import java.util.Date;
import java.util.Map;

/**
 * Created by Hans on 29/10/2015.
 */
public class Event {

  String name;
  TransitionObject transition;
  Map<String,String> attributes;
  Date timestamp;

    public Event(String name, Date timestamp, Map<String,String> attributes){
        this.name = name;
        this.timestamp = timestamp;
        this.attributes = attributes;
    }

    public TransitionObject getTransition() {
        return transition;
    }

    public void setTransition(TransitionObject transition) {
        this.transition = transition;
    }
}
