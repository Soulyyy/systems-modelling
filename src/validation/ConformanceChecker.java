package validation;

import org.processmining.models.connections.GraphLayoutConnection;
import org.processmining.models.graphbased.AbstractGraphEdge;
import org.processmining.models.graphbased.directed.petrinet.PetrinetEdge;
import org.processmining.models.graphbased.directed.petrinet.PetrinetGraph;
import org.processmining.models.graphbased.directed.petrinet.PetrinetNode;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.graphbased.directed.petrinet.impl.PetrinetFactory;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.plugins.pnml.Pnml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Hans on 29/10/2015.
 */
public class ConformanceChecker {
  static Trace[] traces;
  static PetriNet petriNet;

  public static void main(String[] args) {
    PetriNet petriNet = getPetriNet(args[0]);
    EventLog eventLog = new EventLog();
    petriNet.mapStuff(eventLog);

    List<Trace> traces = Arrays.stream(eventLog.getCases()).map(Case::getTrace).collect(Collectors.toList());

    recursiveMethod();


  }

  public static LogResult recursiveMethod() {
    Map<Trace, LogResult> traceLogResultMap = new HashMap<>();
    for (Trace trace : traces) {
      //Tell me what comes back
      LogResult logResult = petriNet.iterate(trace);
      traceLogResultMap.put(trace, logResult);
    }
    if (true) {
      //OR map?
      return new LogResult();
    }
    recursiveMethod();
    return new LogResult();
  }

  public static String calculateConformance() {
    return "TERE";
  }

  public static PetriNet getPetriNet(String fileLocation) {

    //Initialize parser

    PetriNet petriNet = new PetriNet();
    File file = new File(fileLocation);
    try (InputStream inputStream = new FileInputStream(file)) {
      XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
      factory.setNamespaceAware(true);
      XmlPullParser parser = factory.newPullParser();
      parser.setInput(inputStream, null);
      int eventType = parser.getEventType();

      Pnml pnml = new Pnml();
      while (eventType != XmlPullParser.START_TAG) {
        eventType = parser.next();
      }
      if (parser.getName().equals(Pnml.TAG)) {
        pnml.importElement(parser, pnml);
      } else {
        pnml.log(Pnml.TAG, parser.getLineNumber(), "Expected pnml");
      }
      PetrinetGraph net = PetrinetFactory.newInhibitorNet(pnml.getLabel() + " (imported from " + file.getName() + ")");
      Marking marking = new Marking();
      pnml.convertToNet(net, marking, new GraphLayoutConnection(net));
      Collection<org.processmining.models.graphbased.directed.petrinet.elements.Place> places = net.getPlaces();
      Collection<org.processmining.models.graphbased.directed.petrinet.elements.Transition> transitions = net.getTransitions();
      org.processmining.models.graphbased.directed.petrinet.elements.Place aPlace = places.iterator().next();
      Transition aTransition = transitions.iterator().next();

      // to get outgoing edges from a place
      Collection<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> edgesOutP = net.getOutEdges(aPlace);

      //to get ingoing edges to a place
      Collection<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> edgesInP = net.getInEdges(aPlace);

      //to get outgoing edges from a transition
      Collection<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> edgesOutT = net.getOutEdges(aTransition);

      //to get ingoing edges to a transition
      Collection<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> edgesInT = net.getInEdges(aTransition);
      new ArrayList<>(net.getInEdges(aPlace));
      //Transition transition = (Transition)(edgesInP.iterator().next().getTarget());
      //System.out.println(transition.getLabel());
      //PetrinetNode n = edgesOutT.iterator().next().getTarget();
      //System.out.println(n instanceof Transition);

      Function<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>, PetrinetNode> mapInEdges = AbstractGraphEdge::getSource;
      Function<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>, PetrinetNode> mapOutEdges = AbstractGraphEdge::getTarget;

      Function<PetrinetNode, Transition> nodeTransitionFunction = i -> ((i instanceof Transition) ? (Transition) i : null);
      Function<PetrinetNode, Place> nodePlaceFunction = i -> ((i instanceof Place) ? (Place) i : null);

      Function<Place, List<Transition>> mapPlaceEdgeSetIn = i -> (net.getInEdges(i).stream()).map(j -> nodeTransitionFunction.apply(j.getSource())).collect(Collectors.toList());
      Function<Place, List<Transition>> mapPlaceEdgeSetOut = i -> (net.getOutEdges(i).stream()).map(j -> nodeTransitionFunction.apply(j.getTarget())).collect(Collectors.toList());
      Function<Transition, List<Place>> mapTransitionEdgeSetIn = i -> (net.getInEdges(i).stream()).map(j -> nodePlaceFunction.apply(j.getSource())).collect(Collectors.toList());
      Function<Transition, List<Place>> mapTransitionEdgeSetOut = i -> (net.getOutEdges(i).stream()).map(j -> nodePlaceFunction.apply(j.getTarget())).collect(Collectors.toList());

      //Function<PlaceObject, Place>

      Function<Place, PlaceObject> mapPlaces = p -> new PlaceObject(p.getLabel());
      Function<Transition, TransitionObject> mapTransitions = t -> new TransitionObject(t.getLabel());

      //Create empty PlaceObjects and TransitionObjects
      //List<PlaceObject> placeObjects = places.stream().map(mapPlaces::apply).collect(Collectors.toList());
      List<PlaceObject> placeObjects = new ArrayList<>();
      List<TransitionObject> transitionObjects = transitions.stream().map(mapTransitions::apply).collect(Collectors.toList());

      HashMap<String, TransitionObject> transitionObjectCache = new HashMap<>();

      for (Place place : places) {

        PlaceObject placeObject = new PlaceObject(place.getLabel());

        Collection<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> inEdges = net.getInEdges(place);
        Collection<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> outEdges = net.getOutEdges(place);

        //Map edges to Vertices
        List<Transition> inVertices = inEdges.stream().map(mapInEdges::apply).map(nodeTransitionFunction::apply).collect(Collectors.toList());
        List<Transition> outVertices = outEdges.stream().map(mapOutEdges::apply).map(nodeTransitionFunction::apply).collect(Collectors.toList());


        //Transform to our Transitions with only names
        List<TransitionObject> inTransitions = inVertices.stream().map(mapTransitions::apply).collect(Collectors.toList());
        List<TransitionObject> outTransitions = outVertices.stream().map(mapTransitions::apply).collect(Collectors.toList());

        //Check whether node is start or finish
        boolean isFirst = false;
        boolean isLast = false;
        if (inTransitions.size() == 0) {
          isFirst = true;
        }
        if (outTransitions.size() == 0) {
          isLast = true;
        }

        //Verify that multiple objects are not created, add place to respective lists
        for (TransitionObject transitionObject : inTransitions) {
          if (transitionObjectCache.containsKey(transitionObject.name)) {
            transitionObject = transitionObjectCache.get(transitionObject.name);
          } else {
            transitionObjectCache.put(transitionObject.name, transitionObject);
          }
          transitionObject.addOutPlace(placeObject);
          placeObject.addInTransition(transitionObject);

        }

        for (TransitionObject transitionObject : outTransitions) {
          if (transitionObjectCache.containsKey(transitionObject.name)) {
            transitionObject = transitionObjectCache.get(transitionObject.name);
          } else {
            transitionObjectCache.put(transitionObject.name, transitionObject);
          }
          transitionObject.addInPlace(placeObject);
          placeObject.addOutTransition(transitionObject);
        }

        if(isFirst) {
          petriNet.startingPlace = placeObject;
        }
        if(isLast) {
          petriNet.endingPlace = placeObject;
        }

      }


      //Map lists of in and out to objects, equals method only relies on name so this can be done by creating new instances of the object

      //Create intial functions and enhanced functions, that should work


/*      Function<org.processmining.models.graphbased.directed.petrinet.elements.Place, PlaceObject> placeMap = p -> new PlaceObject(p.getId().toString().substring(5, p.getId().toString().length()));
      Function<org.processmining.models.graphbased.directed.petrinet.elements.Place, List<TransitionObject>> mapEdgeToTarget =
          p -> net.getOutEdges(p).stream().map(i -> placeMap.apply((Transition) (i.getTarget()))).collect(Collectors.toList());
      String id = places.iterator().next().getId().toString();
      //First five letters are "node ", probably don't want them
      id = id.substring(5, id.length());
      List<PlaceObject> placeList = places.stream().map(placeMap::apply).collect(Collectors.toList());
      //placeList.forEach(System.out::println);
      Function<List<org.processmining.models.graphbased.directed.petrinet.elements.Place>, List<PlaceObject>> mapPlaceLists = i -> i.stream().map(placeMap::apply).collect(Collectors.<PlaceObject>toList());
      transitions.iterator().next().getLabel();
      Function<org.processmining.models.graphbased.directed.petrinet.elements.Transition, TransitionObject> mapTransition =
          i -> new TransitionObject(i.getLabel(), mapPlaceLists.apply(mapPlaceLists.apply(new ArrayList<>(net.getInEdges(i)))));
      List<TransitionObject> transitionList = transitions.stream().map(mapTransition::apply).collect(Collectors.toList());*/

      //  transitionList.forEach(System.out::println);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return petriNet;
  }
}
