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
    //Sanity checks, whether we are dealing with same objects
    petriNet.startingPlace.outTransitions.stream().forEach(i -> i.inPlaces.stream().forEach(System.out::println));
    System.out.println(petriNet.startingPlace == petriNet.startingPlace.outTransitions.get(0).inPlaces.get(0));
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
      Collection<Place> places = net.getPlaces();

      //Helper functions
      Function<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>, PetrinetNode> mapInEdges = AbstractGraphEdge::getSource;
      Function<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>, PetrinetNode> mapOutEdges = AbstractGraphEdge::getTarget;
      Function<PetrinetNode, Transition> nodeTransitionFunction = i -> ((i instanceof Transition) ? (Transition) i : null);
      Function<Transition, TransitionObject> mapTransitions = t -> new TransitionObject(t.getLabel());

      //Cache to avoid creating extra objects
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

        if (isFirst) {
          petriNet.startingPlace = placeObject;
        }
        if (isLast) {
          petriNet.endingPlace = placeObject;
        }

      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return petriNet;
  }
}
