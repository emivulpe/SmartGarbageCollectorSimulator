package uk.ac.glasgow.etparser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import uk.ac.glasgow.etparser.events.*;
import uk.ac.glasgow.etparser.handlers.*;

/**
 *  This class parses a file into lines, keeps a list of event
 *         handlers and every time an event occurs it notifies them. It also
 *         prints a report of the percentage and kind of errors caused by
 *         attempt to access not born or dead objects.
 * @author Emi
 * @version 1.0
 */
public class ETParser {
	/**
	 * List of registered objects to handle events.
	 */
	private List<EventHandler> handlers;
	/**
	 * This variable counts the lines read so far.
	 */
	private int lines;
	private InputStream input;
	/**
	 * This heap is analogous to the memory. It contains all the objects
	 * allocated and also keeps track of all the objects tried to be accessed at
	 * any point of the program.
	 */
	private SimulatedHeap heap;

	/**
	 * Constructor initializing the ETParser which takes an InputStream and does all its work using the information from the stream.
	 * @param input
	 *            : the file to be parsed
	 */

	public ETParser(InputStream input) {
		this.input=input;
		handlers = new ArrayList<EventHandler>();
		lines = 0;
		heap = SimulatedHeap.getTheHeap();
		initialiseHandlers();

	}

	/**
	 * 
	 *            For each line of the scanner create an event and notify the
	 *            handlers for it.
	 */

	public void processFile() {
		Scanner scanner = new Scanner(input);
		EventFactory factory = new EventFactory();
		while (scanner.hasNextLine()) {

			String nextLine = scanner.nextLine();
			System.out.println(nextLine + " next line");
			lines++;
			Event event = factory.createEvent(nextLine);
			notifyHandlers(event);

		}
		scanner.close();

	}

	/**
	 * @return the number of lines read by the parser
	 */
	public int getLines() {
		return lines;
	}

	/**
	 * A method that notifies all event handlers of the occurrence of an event.
	 * @param e
	 *            : a new event read by the parser When an event occurs all
	 *            handlers are notified of it
	 */
	public void notifyHandlers(Event e) {
		for (EventHandler eh : handlers) {
			eh.handle(e);

		}
	}

	/**
	 * @param eh
	 *            event handler be added to the list of handlers
	 */
	public void registerHandler(EventHandler eh) {
		handlers.add(eh);
	}

	/**
	 * All handlers instances of EventReport interface print their final report
	 * for statistics.
	 */
	public void printReport() {
		for (EventHandler eh : handlers) {
			if (eh instanceof EventReport) {
				System.out.println(((EventReport) eh).finalReport());

			}

		}
	}

	/**
	 * Adds to the list of handlers all necessary EventHandlers for the program.
	 */
	public void initialiseHandlers() {
		registerHandler(heap);
		EventHandler creation = new CountCreation();
		registerHandler(creation);
		EventHandler legal = new CountLegal();
		registerHandler(legal);
		EventHandler dead = new CountDead();
		registerHandler(dead);
		EventHandler multiple = new CountMultipleCreations();
		registerHandler(multiple);
		EventHandler notBorns = new CountNotBorn();
		registerHandler(notBorns);
		EventHandler logger = new ErrorLogger();
		registerHandler(logger);
		EventHandler livesize = new LiveSize();
		registerHandler(livesize);

	}
	/**
	 * Getter method for the registered handlers.
	 * @return the list of currently registered handlers
	 */
	public List<EventHandler> getHandlers(){
		return handlers;
	}

}
