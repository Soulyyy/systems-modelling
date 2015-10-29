import java.util.Date;
import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.extension.std.XLifecycleExtension;
import org.deckfour.xes.extension.std.XTimeExtension;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;

public class XESTest {

	public static void main(String[] args) {
		try {
			XLog log = XLogReader.openLog(args[0]);
			for(XTrace trace:log){
				String traceName = XConceptExtension.instance().extractName(trace);
				XAttributeMap caseAttributes = trace.getAttributes();
				for(XEvent event : trace){
					String activityName = XConceptExtension.instance().extractName(event);
					Date timestamp = XTimeExtension.instance().extractTimestamp(event);
					String eventType = XLifecycleExtension.instance().extractTransition(event);
					XAttributeMap eventAttributes = event.getAttributes();
					for(String key :eventAttributes.keySet()){
						String value = eventAttributes.get(key).toString();
					}
					for(String key :caseAttributes.keySet()){
						String value = caseAttributes.get(key).toString();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
