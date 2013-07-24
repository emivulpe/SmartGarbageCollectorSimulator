package uk.ac.glasgow.etparser.handlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import uk.ac.glasgow.etparser.ACommandLineParser.WayToDeal;
import uk.ac.glasgow.etparser.ObjectClass.EventType;
import uk.ac.glasgow.etparser.ObjectClass;
import uk.ac.glasgow.etparser.ObjectEventRecord;
import uk.ac.glasgow.etparser.events.CreationEvent;
import uk.ac.glasgow.etparser.events.Event;
import uk.ac.glasgow.etparser.events.Event.Check;

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
	private HashMap<String, ObjectClass> objectStates;
	/**
	 * A set of all the objects tried to be accessed ever including the once
	 * that were not born.
	 */
	private Set<String> processedObjects;



	@SuppressWarnings("unused")
	private WayToDeal dealWithPreaccess, dealWithPostAccess;

	/**
	 * Initializes the class variables. Private because of Singleton design
	 * pattern.
	 */
	private SimulatedHeap() {
		timeSequence = 0;
		timeSize = 0;
		timeMethod = 0;
		objectStates = new HashMap<String, ObjectClass>();
		processedObjects = new HashSet<String>();

	}

	public void setDealWithPreaccess(WayToDeal wayToDeal){

		dealWithPreaccess=wayToDeal;



	}

	public void setDealWithPostaccess(WayToDeal wayToDeal){
		dealWithPostAccess=wayToDeal;

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
		if (!existsInHeap && e instanceof CreationEvent ) {
			e.setCheck(Check.CREATION);
			allocateObject((CreationEvent) e);

		}

		// free but not status "A"- preaccess
		else if (!existsInHeap && !currentEventStatus.equalsIgnoreCase("A")) {
			e.setCheck(Check.NOTBORN);
			decisionMakerPreaccess(e);

			// dead-postaccess
		} else if (existsInHeap && objectStates.get(currentObjectID).getLastEvent()==EventType.DEATH) {
			e.setCheck(Check.DEAD);
			decisionMakerPostaccess();

		}

		// occupied and alive
		else if (existsInHeap && objectStates.get(currentObjectID).getLastEvent()!=EventType.DEATH) {
			e.setCheck(Check.LEGAL);
			updateObject(e);

		}

		else if (existsInHeap && currentEventStatus.equalsIgnoreCase("A")) {

			e.setCheck(Check.CREATED);

		}
		System.out.println(getNumObjects() + " total objects in heap"
				+ processedObjects);
	}

	private void allocateObject(CreationEvent e) {
		String currentObjectID = e.getObjectID();
		int size=e.getSize();
		ObjectClass object=new ObjectClass(size,timeSequence,currentObjectID);
//		ObjectEventRecord record = new ObjectEventRecord(e);
		objectStates.put(currentObjectID, object);
		e.setCheck(Check.CREATION);
		CreationEvent ce = (CreationEvent) e;
		timeSize += ce.getSize();
		System.out
		.println(timeSize
				+ " timesizeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");

	}


	private void allocateObjectCheater(Event e) {
		String currentObjectID = e.getObjectID();
		ObjectClass object=new ObjectClass(0,timeSequence,currentObjectID);
		
//		ObjectEventRecord record = new ObjectEventRecord(e);
		objectStates.put(currentObjectID, object);
		e.setCheck(Check.CREATION);
		CreationEvent ce = new CreationEvent(e);
		timeSize += ce.getSize();
		System.out
		.println(timeSize
				+ " timesizeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");

	}

	private void updateObject(Event e) {
		String currentObjectID = e.getObjectID();
		ObjectClass accessedObject=objectStates.get(currentObjectID);
		
		String currentEventStatus = e.getStatus();
//		ObjectEventRecord record = objectStates.get(currentObjectID);
//		record.updateRecord(e);
		
		accessedObject.updateEvent(timeSequence, e.getStatus());
		objectStates.put(currentObjectID, accessedObject);
		e.setCheck(Check.LEGAL);
//		System.out.println(objectStates.get(currentObjectID).isAlive());
		if (currentEventStatus.equalsIgnoreCase("M")
				|| currentEventStatus.equalsIgnoreCase("E")) {
			timeMethod++;
		}

	}

	private void decisionMakerPreaccess(Event e) {

		switch (dealWithPreaccess){
		case MOVE:
			handleAllocateAtFirstAccess(e);
			break;
		default:
			handleIgnorePreaccess();

		}
	}

	private void decisionMakerPostaccess() {
		switch (dealWithPostAccess){
		case MOVE:
			handleMoveDeath();
			break;
		default:
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
	public ObjectClass getRecord(String oid) {
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


	private void handleIgnorePreaccess() {
		System.out.println("ignore preaccess");
		// as this method just ignores preaccess it doesn't do anything

	}

	private void handleAllocateAtFirstAccess(Event e) {
		System.out.println("allocate at first access");
		allocateObjectCheater(e);
		updateObject(e);

	}

	private void handleIgnorePostaccess() {
		System.out.println("ignore postaccess");
		// as this method just ignores postaccess it doesn't do anything

	}

	// no need for this in my opinion. we don't need an object to be dead to
	// collect it
	private void handleMoveDeath() {
		System.out.println("move death");

	}

	public void removeRecord(String objectID){
		objectStates.remove(objectID);

	}
	
	
	public List<ObjectClass> getListOfObjectClassSorted(){
		ArrayList<ObjectClass> listOfObjects=new ArrayList<ObjectClass>();
		for (ObjectClass obj:objectStates.values()){
			listOfObjects.add(obj);
			
		}
		
	
		
		Collections.sort(listOfObjects, new Comparator<ObjectClass>(){
			public int compare(ObjectClass o1,ObjectClass o2){
				return Integer.compare(o1.getTimeOfLastEvent(),o2.getTimeOfLastEvent());
				
			}
		});
		
		return listOfObjects;
	}
	
	

}
