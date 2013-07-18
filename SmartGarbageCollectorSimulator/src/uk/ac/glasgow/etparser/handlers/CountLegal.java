package uk.ac.glasgow.etparser.handlers;

import java.util.HashSet;
import java.util.Set;

import uk.ac.glasgow.etparser.events.Event;

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
		if (e.getCheck().equalsIgnoreCase("legal")) {
			legals.add(e.getObjectID());
			System.out.println("Object with id " + e.getObjectID()
					+ " has been updated.");
		}

	}

	/**
	 * Gives a final report of what percentage of total objects were updated
	 * successfully.
	 */
	@Override
	public String finalReport() {

		return (float) legals.size()
				/ SimulatedHeap.getTheHeap().getNumObjects() * PERCENTAGE
				+ " % objects were updated successfully";
	}

}
