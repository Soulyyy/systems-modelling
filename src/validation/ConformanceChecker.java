package validation;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.extension.std.XLifecycleExtension;
import org.deckfour.xes.extension.std.XTimeExtension;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
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
    EventLog eventLog = getEventLog(args[1]);
    petriNet.mapStuff(eventLog);
    for (Case caseObj : eventLog.getCases()) {
      Trace trace = petriNet.iterateTrace(caseObj.getTrace());
      System.out.println(trace);
    }

    /*traces = Arrays.stream(eventLog.getCases()).map(Case::getTrace).collect(Collectors.toList());*/
  }

  public static void traverseNet(PetriNet net, Trace trace) {
    net.iterate(trace);
    System.out.println(trace.consumedTokens);
    System.out.println(trace.missingTokens);
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
      petriNet.placeCount = places.size();

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
      //All nodes have hit cache, so we can do this
      petriNet.transitions = transitionObjectCache.values().stream().collect(Collectors.toList());
    } catch (Exception e) {
      e.printStackTrace();
    }

    return petriNet;
  }

  public static EventLog getEventLog(String inputFile) {
    Map<String, Trace> traces = new HashMap<>();
    List<Case> cases = new ArrayList<Case>();
    try {
      XLog log = XLogReader.openLog(inputFile);
      for (XTrace trace : log) {
        String traceName = XConceptExtension.instance().extractName(trace);
        XAttributeMap caseAttributes = trace.getAttributes();
        List<Event> events = new ArrayList<Event>();
        Map<String, String> caseAttrs = new HashMap<>();
        String traceAbr = "";
        for (XEvent event : trace) {
          String activityName = XConceptExtension.instance().extractName(event);
          traceAbr += activityName;
          Date timestamp = XTimeExtension.instance().extractTimestamp(event);
          String eventType = XLifecycleExtension.instance().extractTransition(event);
          XAttributeMap eventAttributes = event.getAttributes();
          Map<String, String> eventAttrs = new HashMap<>();
          for (String key : eventAttributes.keySet()) {
            eventAttrs.put(key, eventAttributes.get(key).toString());
          }
          for (String key : caseAttributes.keySet()) {
            caseAttrs.put(key, caseAttributes.get(key).toString());
          }
          events.add(new Event(activityName, timestamp, eventAttrs));
        }
        if(traces.get(traceAbr) == null){
          traces.put(traceAbr, new Trace(events));
        }
        cases.add(new Case(Integer.parseInt(traceName), traces.get(traceAbr), caseAttrs));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    cases.stream().forEach(System.out::println);
    return new EventLog(cases);
  }

  public static double computeFitness(List<Trace> traces) {
    int missingTokens = traces.stream().mapToInt(i -> i.totalTraces * i.missingTokens).reduce((j, h) -> j + h).getAsInt();
    int remainingTokens = traces.stream().mapToInt(i -> i.totalTraces * i.remainingTokens).reduce((j, h) -> j + h).getAsInt();
    int consumedTokens = traces.stream().mapToInt(i -> i.totalTraces * i.consumedTokens).reduce((j, h) -> j + h).getAsInt();
    int producedTokens = traces.stream().mapToInt(i -> i.totalTraces * i.producedTokens).reduce((j, h) -> j + h).getAsInt();
    return (1 - ((double) missingTokens / consumedTokens)) * 0.5 + (1 - ((double) remainingTokens / producedTokens)) * 0.5;
  }
}


