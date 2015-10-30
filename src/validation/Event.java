package validation;

import java.security.Timestamp;
import java.util.Date;
import java.util.Map;

/**
 * Created by Hans on 29/10/2015.
 */
public class Event {

  String name;
<<<<<<< HEAD
  TransitionObject transition;
=======
  Transition transition;
  Map<String,String> attributes;
  Date timestamp;

    public Event(String name, Date timestamp, Map<String,String> attributes){
        this.name = name;
        this.timestamp = timestamp;
        this.attributes = attributes;
    }

    public Transition getTransition() {
        return transition;
    }

    public void setTransition(Transition transition) {
        this.transition = transition;
    }
>>>>>>> b99eefef4a1b563a6b0548db299adf7594db985e
}
