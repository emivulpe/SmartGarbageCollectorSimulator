package uk.ac.glasgow.etparser.handlers.EventReporters;

import java.util.HashSet;
import java.util.Set;

import uk.ac.glasgow.etparser.ETParser;
import uk.ac.glasgow.etparser.events.Event;
import uk.ac.glasgow.etparser.events.Event.Check;
import uk.ac.glasgow.etparser.handlers.EventHandler;

/**
 * This class keeps track of all objects tried to be accessed before they were
 * born.
 * 
 * @author Emi
 * @version 1.0
 * 
 */
public class CountNotBorn implements EventHandler, EventReport {
	/**
	 * Set of all objects tried to be accessed before they were born.
	 */
	private Set<String> notBorn;

	/**
	 * Initializes the empty set of objects tried to be accessed before they
	 * were born.
	 */
	public CountNotBorn() {
		notBorn = new HashSet<String>();
	}

	/**
	 * 
	 * @return the set of all objects tried to be accessed before they were
	 *         born.
	 */
	public Set<String> getNotBorns() {
		return notBorn;
	}

	/**
	 * Receives an event as input and if the object has not been created and the
	 * event isn't a creation it records it, otherwise it ignores it.
	 * 
	 * @param e
	 *            an event to be recorded
	 */
	@Override
	public void handle(Event e) {
		if (e.getCheck().equals(Check.NOTBORN)) {
			notBorn.add(e.getObjectID());
			// System.out.println("Object with id " + e.getObjectID()
			// + " is not born yet.");
		}

	}

	/**
	 * Gives a final report of what percentage of total objects were accessed
	 * before they were born.
	 */
	@Override
	public String finalReport() {
		if (ETParser.getLogger() != null) {
			ETParser.getLogger()
					.getLogger()
					.info((float) notBorn.size() / totalObjectsInHeap
							* PERCENTAGE + " % objects cause not born error.");
		}
		return (float) notBorn.size() / totalObjectsInHeap * PERCENTAGE
				+ " % objects cause not born error";
	}

}
