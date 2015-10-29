package validation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hans on 29/10/2015.
 */
public class Transition {

  String name;

  Event event;

  //These lists  represent directed edges
  List<Place> inPlaces;
  List<Place> outPlaces;

  public Transition() {

  }

  public Transition(String name, Event event) {
    this.name = name;
    this.event = event;
    this.inPlaces = new ArrayList<>();
    this.outPlaces = new ArrayList<>();
  }

  public void addInPlace(Place place) {
    this.inPlaces.add(place);
  }

  public void addOutEdge(Place place) {
    this.outPlaces.add(place);
  }
}
