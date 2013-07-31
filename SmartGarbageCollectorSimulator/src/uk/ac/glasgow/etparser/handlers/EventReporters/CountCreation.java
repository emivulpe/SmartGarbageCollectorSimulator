package uk.ac.glasgow.etparser.handlers.EventReporters;

import java.util.HashSet;
import java.util.Set;

import uk.ac.glasgow.etparser.ETParser;
import uk.ac.glasgow.etparser.events.Event;
import uk.ac.glasgow.etparser.events.Event.Check;
import uk.ac.glasgow.etparser.handlers.EventHandler;

/**
 * This class keeps track of all successfully created objects.
 * 
 * @author Emi
 * @version 1.0
 * 
 */
public class CountCreation implements EventHandler, EventReport {
	/**
	 * Set of all created objects.
	 */
	private Set<String> created;

	/**
	 * Initializes the empty set of created objects.
	 */
	public CountCreation() {
		created = new HashSet<String>();
	}

	/**
	 * 
	 * @return the set of all created objects.
	 */
	public Set<String> getCreated() {
		return created;
	}

	/**
	 * Receives an event as input and if it is a creation it records it,
	 * otherwise it ignores it.
	 * 
	 * @param e
	 *            an event to be recorded
	 */
	@Override
	public void handle(Event e) {

		if (e.getCheck() != null && e.getCheck().equals(Check.CREATION)) {
			created.add(e.getObjectID());
			// System.out.println("Object with id " + e.getObjectID()
			// + " has been created.");
		}

	}

	/**
	 * Gives a final report of what percentage of total objects were created
	 * successfully.
	 */
	@Override
	public String finalReport() {
		// if statistics logger wanted by the user- log the info into the
		// file...
		// do the same with all other evert reporters!
		if (ETParser.getLogger() != null) {
			ETParser.getLogger()
					.getLogger()
					.info((float) created.size() / totalObjectsInHeap
							* PERCENTAGE
							+ " % objects were created successfully");
		}
		return (float) created.size() / totalObjectsInHeap * PERCENTAGE
				+ " % objects were created successfully";
	}

}
