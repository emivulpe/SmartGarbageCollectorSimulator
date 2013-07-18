package uk.ac.glasgow.etparser.handlers;

import java.io.OutputStreamWriter;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import uk.ac.glasgow.etparser.events.Event;

/**
 * A class to record all errors created by accesses to not born or dead objects
 * or errors caused by trying to create an object more than once
 * 
 * @author Emi
 * @version 1.0
 * 
 */
public class ErrorLogger implements EventHandler {

	/**
	 * Logger that records the errors.
	 */
	private final Logger logger = Logger.getLogger(ErrorLogger.class.getName());

	/**
	 * Initializes the console appender and the layout of the logger.
	 */
	public ErrorLogger() {
		ConsoleAppender ca = new ConsoleAppender();
		ca.setName("My appender");
		ca.setWriter(new OutputStreamWriter(System.err));
		ca.setLayout(new PatternLayout("%-5p [%t]: %m%n"));
		logger.addAppender(ca);

	}

	/**
	 * 
	 * @return the logger
	 */
	public Logger getLogger() {
		return logger;
	}

	/**
	 * Method to handle an event. If there is illegal access to an object a
	 * respecting message is recorded. Otherwise the logger ignores the event.
	 */
	@Override
	public void handle(Event e) {
		String currentObjectID = e.getObjectID();
		if (e.getCheck().equalsIgnoreCase("dead")) {
			logger.warn("The object with id " + currentObjectID
					+ " is dead so you cannot update it!");

		} else if (e.getCheck().equalsIgnoreCase("not born")) {
			logger.warn("The object with id " + currentObjectID
					+ " is not born so you cannot update it!");
		} else if (e.getCheck().equalsIgnoreCase("created")) {
			logger.warn("The object with id " + currentObjectID
					+ " is already created so you cannot create it again!");

		}

	}

}
