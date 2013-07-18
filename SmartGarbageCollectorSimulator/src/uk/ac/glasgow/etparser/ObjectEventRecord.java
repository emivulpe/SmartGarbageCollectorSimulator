package uk.ac.glasgow.etparser;

import uk.ac.glasgow.etparser.events.Event;

public class ObjectEventRecord {
	private Event lastEvent;
	private int numEvents, numUpdates, numMethodCalls;
	private boolean isAlive = false;

	public ObjectEventRecord(final Event e) {
		isAlive = true;
		lastEvent = e;
		numEvents = 1;
		numUpdates = 0;
		numMethodCalls = 0;

	}

	public final int getNumEvents() {
		return numEvents;
	}

	public final int getNumUpdates() {
		return numUpdates;
	}

	public final int getNumMethodCalls() {
		return numMethodCalls;
	}

	public final Event getLastEvent() {
		return lastEvent;
	}

	public final void updateRecord(final Event e) {
		lastEvent = e;
		numEvents++;
		if (e.getStatus().equalsIgnoreCase("U")) {
			numUpdates++;
		}
		
		if (e.getStatus().equalsIgnoreCase("M")) {
			numMethodCalls++;
		}

		if (e.getStatus().equalsIgnoreCase("D")) {
			isAlive = false;
		}

	}

	public final boolean isAlive() {
		return isAlive;
	}
	
	

	public void killObject() {
		isAlive = false;
	}

	public final String toString() {
		return "Event: " + lastEvent;
	}
}
