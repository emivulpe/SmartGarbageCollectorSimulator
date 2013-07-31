package uk.ac.glasgow.etparser.handlers.EventReporters;

import java.util.HashSet;
import java.util.Set;

import uk.ac.glasgow.etparser.ETParser;
import uk.ac.glasgow.etparser.events.Event;
import uk.ac.glasgow.etparser.events.Event.Check;
import uk.ac.glasgow.etparser.handlers.EventHandler;

/**
 * This class keeps track of all live objects.
 * 
 * @author Emi
 * @version 1.0
 * 
 */
public class CountLegal implements EventHandler, EventReport {
	/**
	 * Set of all live objects.
	 */
	private Set<String> legals;

	/**
	 * Initializes the empty set of live objects.
	 */
	public CountLegal() {
		legals = new HashSet<String>();
	}

	/**
	 * 
	 * @return the set of all living objects.
	 */
	public Set<String> getLegal() {
		return legals;
	}

	/**
	 * Receives an event as input and if it is a legal access of an object it
	 * records it, otherwise it ignores it.
	 * 
	 * @param e
	 *            an event to be recorded
	 */
	@Override
	public void handle(Event e) {
		if (e.getCheck() != null && e.getCheck().equals(Check.LEGAL)) {
			legals.add(e.getObjectID());
			// System.out.println("Object with id " + e.getObjectID()
			// + " has been updated.");
		}

	}

	/**
	 * Gives a final report of what percentage of total objects were updated
	 * successfully.
	 */
	@Override
	public String finalReport() {

		if (ETParser.getLogger() != null) {
			ETParser.getLogger()
					.getLogger()
					.info((float) legals.size() / totalObjectsInHeap
							* PERCENTAGE
							+ " % objects were updates successfully");
		}

		return (float) legals.size() / totalObjectsInHeap * PERCENTAGE
				+ " % objects were updated successfully";
	}

}
