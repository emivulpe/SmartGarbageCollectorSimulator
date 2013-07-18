package uk.ac.glasgow.etparser.handlers;

/**
 * All classes which produce a final report must implement that interface.
 * 
 * @author Emi
 * 
 */
public interface EventReport {
	/**
	 * Used for calculating the result as a percentage.
	 */
	public static final int PERCENTAGE = 100;

	/**
	 * 
	 * @return a message of the current status of the program.
	 */
	public String finalReport();

}
