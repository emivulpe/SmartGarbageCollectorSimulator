package uk.ac.glasgow.etparser.handlers;

import uk.ac.glasgow.etparser.events.Event;

/**
 * All classes which respond to events must implement this interface.
 * 
 * @author Emi
 * @version 1.0
 * 
 */
public interface EventHandler {
	/**
	 * 
	 * @param e
	 *            event to be handled
	 */

	public void handle(Event e);

}
