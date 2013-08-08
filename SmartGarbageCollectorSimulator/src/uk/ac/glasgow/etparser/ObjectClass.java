package uk.ac.glasgow.etparser;

import uk.ac.glasgow.etparser.events.Event.TypeOfEvent;

public class ObjectClass {

	private int size;
	private int timeOfLastEvent;
	private String id;

	private TypeOfEvent lastEvent;

	public ObjectClass(int s, int t, String id) {
		size = s;
		timeOfLastEvent = t;
		this.id = id;
		lastEvent = TypeOfEvent.ALLOCATION;
//		System.out.println("object id "+id+" size "+size);
	}

	public int getSize() {
		return size;
	}

	public int getTimeOfLastEvent() {
		return timeOfLastEvent;
	}

	public String getID() {
		return id;
	}

	public TypeOfEvent getLastEvent() {
		return lastEvent;
	}

	public void updateEvent(int time, TypeOfEvent event) {
		if (lastEvent == TypeOfEvent.DEATH) {
			return;

		}
		this.timeOfLastEvent = time;
		lastEvent = event;

	}

}
