package validation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hans on 29/10/2015.
 */
public class TransitionObject {

  String name;

  Event event;

  //These lists  represent directed edges
  List<PlaceObject> inPlaces;
  List<PlaceObject> outPlaces;

  public TransitionObject() {

  }

  public TransitionObject(String name) {
    this(name, null);
  }

  public TransitionObject(String name, Event event) {
    this.name = name;
    this.event = event;
    this.inPlaces = new ArrayList<>();
    this.outPlaces = new ArrayList<>();
  }

  public TransitionObject(String name, List<PlaceObject> in, List<PlaceObject> out) {
    this.name = name;
    this.inPlaces = in;
    this.outPlaces = out;
  }

  public void addInPlace(PlaceObject place) {
    this.inPlaces.add(place);
  }

  public void addOutPlace(PlaceObject place) {
    this.outPlaces.add(place);
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    TransitionObject that = (TransitionObject) o;

    if (name != null ? !name.equals(that.name) : that.name != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return name != null ? name.hashCode() : 0;
  }
}
