package uk.ac.glasgow.etparser.handlers;

import org.apache.log4j.Logger;

/**
 * This class produces a file with statistics of the current state of the
 * program and percentage of errors and successful creations.
 * 
 * @author Emi
 * 
 */
public class StatisticsLogger {

	/**
	 * Logger to record the inf0rmation.
	 */
	private final Logger logger;

	/**
	 * Initializes the name of the logger.
	 */
	public StatisticsLogger() {
		logger = Logger.getLogger("stats");
	}

	/**
	 * 
	 * @return the logger
	 */
	public Logger getLogger() {
		return logger;
	}

}
