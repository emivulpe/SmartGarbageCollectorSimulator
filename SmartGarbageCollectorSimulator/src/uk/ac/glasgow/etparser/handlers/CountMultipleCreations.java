package uk.ac.glasgow.etparser.handlers;

import java.util.HashSet;
import java.util.Set;

import uk.ac.glasgow.etparser.events.Event;

/**
 * This class keeps track of all objects tried to be created more than
 * once.
 * 
 * @author Emi
 * @version 1.0
 * 
 */
public class CountMultipleCreations implements EventHandler, EventReport {
	/**
	 * Set of all objects tried to be created more than once.
	 */
	private Set<String> multiples;

	/**
	 * Initializes the empty set of objects tried to be created more than once.
	 */
	public CountMultipleCreations() {
		multiples = new HashSet<String>();
	}

	/**
	 * 
	 * @return the set of all objects tried to be created more than once.
	 */
	public Set<String> getMultiples() {
		return multiples;
	}

	/**
	 * Receives an event as input and if the object has already been created it
	 * records it, otherwise it ignores it.
	 * 
	 * @param e
	 *            an event to be recorded
	 */
	@Override
	public void handle(Event e) {
		if (e.getCheck().equalsIgnoreCase("created")) {
			multiples.add(e.getObjectID());
			System.out.println("Object with id " + e.getObjectID()
					+ " has been created more than once.");
		}

	}

	/**
	 * Gives a final report of what percentage of total objects were tried to be
	 * created more than once.
	 */
	@Override
	public String finalReport() {
		// TODO Auto-generated method stub
		return (float) multiples.size()
				/ SimulatedHeap.getTheHeap().getNumObjects() * PERCENTAGE
				+ " % objects were created more than once";
	}

}
