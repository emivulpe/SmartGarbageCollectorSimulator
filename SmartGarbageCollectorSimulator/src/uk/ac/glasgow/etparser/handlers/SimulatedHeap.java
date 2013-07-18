package uk.ac.glasgow.etparser.handlers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import uk.ac.glasgow.etparser.ObjectEventRecord;
import uk.ac.glasgow.etparser.events.CreationEvent;
import uk.ac.glasgow.etparser.events.Event;

/**
 * This class simulates the heap and keeps track of the object ids and the last
 * event that happened to them as well as the total objects tried to be accessed
 * ever.
 * 
 * @author Emi
 * @version 1.0
 * 
 * 
 */

public class SimulatedHeap implements EventHandler {
	/**
	 * This is analogous to the real heap memory.
	 */
	private static SimulatedHeap theHeap = null;
	/**
	 * Measures time sequentially (1, 2, 3...).
	 */
	private int timeSequence;
	/**
	 * Measures time as function of the allocated objects' sizes.
	 */
	private int timeSize;

	/**
	 * Measures time in terms of method entry and method exit.
	 */
	private int timeMethod;
	/**
	 * A hash map that keeps the object id and the last event that happened to
	 * that object.
	 */
	private HashMap<String, ObjectEventRecord> objectStates;
	/**
	 * A set of all the objects tried to be accessed ever including the once
	 * that were not born.
	 */
	private Set<String> processedObjects;
	


	@SuppressWarnings("unused")
	private String dealWithPreaccess, dealWithPostAccess;

	/**
	 * Initializes the class variables. Private because of Singleton design
	 * pattern.
	 */
	private SimulatedHeap() {
		timeSequence = 0;
		timeSize = 0;
		timeMethod = 0;
		objectStates = new HashMap<String, ObjectEventRecord>();
		processedObjects = new HashSet<String>();
		displayChoices();
		askForPreaccess();
		askForPostaccess();
	}

	/**
	 * 
	 * @return an instance of the heap. It is global.
	 */
	public static SimulatedHeap getTheHeap() {
		if (theHeap == null) {
			theHeap = new SimulatedHeap();

		}
		return theHeap;
	}

	/**
	 * Checks whether the event is legal and if it is, it updates the event
	 * record in the heap and updates the time.
	 * 
	 * @param e
	 *            instance of the Event super class
	 */
	@Override
	public void handle(Event e) {
		processedObjects.add(e.getObjectID());
		timeSequence++;

		boolean existsInHeap = objectStates.get(e.getObjectID()) != null;
		String currentObjectID = e.getObjectID();
		String currentEventStatus = e.getStatus();

		// free and status "A"
		if (!existsInHeap && currentEventStatus.equalsIgnoreCase("A")) {
			allocateObject(e);

		}

		// free but not status "A"- preaccess
		else if (!existsInHeap && !currentEventStatus.equalsIgnoreCase("A")) {

			decisionMakerPreaccess(e);

			// dead-postaccess
		} else if (existsInHeap && !objectStates.get(currentObjectID).isAlive()) {

			decisionMakerPostaccess();

		}

		// occupied and alive
		else if (existsInHeap && objectStates.get(currentObjectID).isAlive()) {
			updateObject(e);

		}

		else if (existsInHeap && currentEventStatus.equalsIgnoreCase("A")) {

			e.setCheck("created");

		}
		System.out.println(getNumObjects() + " total objects in heap"
				+ processedObjects);
	}

	private void allocateObject(Event e) {
		String currentObjectID = e.getObjectID();
		ObjectEventRecord record = new ObjectEventRecord(e);
		objectStates.put(currentObjectID, record);
		e.setCheck("creation");
		CreationEvent ce = (CreationEvent) e;
		timeSize += ce.getSize();
		System.out
				.println(timeSize
						+ " timesizeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");

	}

	
	private void allocateObjectCheater(Event e) {
		String currentObjectID = e.getObjectID();
		ObjectEventRecord record = new ObjectEventRecord(e);
		objectStates.put(currentObjectID, record);
		e.setCheck("creation");
		CreationEvent ce = new CreationEvent(e);
		timeSize += ce.getSize();
		System.out
				.println(timeSize
						+ " timesizeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");

	}

	private void updateObject(Event e) {
		String currentObjectID = e.getObjectID();
		String currentEventStatus = e.getStatus();
		ObjectEventRecord record = objectStates.get(currentObjectID);
		record.updateRecord(e);
		objectStates.put(currentObjectID, record);
		e.setCheck("legal");
		System.out.println(objectStates.get(currentObjectID).isAlive());
		if (currentEventStatus.equalsIgnoreCase("M")
				|| currentEventStatus.equalsIgnoreCase("E")) {
			timeMethod++;
		}

	}

	private void decisionMakerPreaccess(Event e) {
		if (dealWithPreaccess.equalsIgnoreCase("First access")) {
			handleAllocateAtFirstAccess(e);
		} else {
			handleIgnorePreaccess();
		}
	}

	private void decisionMakerPostaccess() {
		if (dealWithPreaccess.equalsIgnoreCase("Move")) {
			handleMoveDeath();
		} else {
			handleIgnorePostaccess();
		}
	}

	/**
	 * 
	 * @return the number of objects tried to be accessed ever.
	 */
	public int getNumObjects() {
		return processedObjects.size();
	}

	/**
	 * 
	 * @param oid
	 *            the id of the object we want to access in the heap.
	 * @return the last event record for the given object.
	 */
	public ObjectEventRecord getRecord(String oid) {
		return objectStates.get(oid);
	}

	/**
	 * 
	 * @return the current time expressed as sequence.
	 */
	public int getTimeSequence() {
		return timeSequence;
	}

	/**
	 * 
	 * @return the current time expressed as allocated object size.
	 */
	public int getTimeSize() {
		return timeSize;
	}

	/**
	 * 
	 * @return the current time expressed in term of method entry and exit.
	 */
	public int getTimeMethod() {
		return timeMethod;
	}

	private void displayChoices() {

		System.out
				.println("Hello, dear user! Before you start the smart garbage collector simulator"
						+ " you must choose how to deal with pre-access and post-access errors.");
		System.out.println();
	}

	private void askForPreaccess() {
		System.out.println("First choose dealing with pre-access errors:");
		System.out.println();
		System.out.println("Enter 'Ignore' to ignore them.");
		System.out
				.println("Enter 'Beginning' to allocate them at the beginning of the program.");
		System.out
				.println("Enter 'First access' to allocate them at the first attempt to access unborn objects");
		System.out.println();
		Scanner scanner = new Scanner(System.in);
		String preaccess = scanner.nextLine();
		while (!preaccess.equalsIgnoreCase("Ignore")
				&& !preaccess.equalsIgnoreCase("Beginning")
				&& !preaccess.equalsIgnoreCase("First access")) {
			System.out.println("Please enter a valid option");
			preaccess = scanner.next();
		}

		System.out.println();
		this.dealWithPreaccess = preaccess;
		scanner.close();
	}

	private void askForPostaccess() {

		System.out.println("Now choose dealing with post-access errors");
		System.out.println();
		System.out.println("Enter 'Ignore' to ignore them.");
		System.out
				.println("Enter 'Move' to kill objects at the end of the program.");
		System.out.println("Enter 'Don't count' not to count these errors");
		Scanner scanner = new Scanner(System.in);
		String postaccess = scanner.nextLine();
		while (!postaccess.equalsIgnoreCase("Ignore")
				&& !postaccess.equalsIgnoreCase("Move")
				&& !postaccess.equalsIgnoreCase("Don't count")) {
			System.out.println("Please enter a valid option");
			postaccess = scanner.next();
		}
		this.dealWithPostAccess = postaccess;
		scanner.close();
	}

	private void handleIgnorePreaccess() {
		// as this method just ignores preaccess it doesn't do anything

	}

	private void handleAllocateAtFirstAccess(Event e) {
		allocateObjectCheater(e);
		updateObject(e);

	}

	private void handleIgnorePostaccess() {
		// as this method just ignores postaccess it doesn't do anything

	}

	// no need for this in my opinion. we don't need an object to be dead to
	// collect it
	private void handleMoveDeath() {

	}
	
	public void removeRecord(String objectID){
		theHeap.removeRecord(objectID);
	}

}
