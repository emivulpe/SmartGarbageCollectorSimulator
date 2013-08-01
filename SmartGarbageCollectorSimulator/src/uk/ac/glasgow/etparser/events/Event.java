package uk.ac.glasgow.etparser.events;

import java.util.Scanner;

public class Event {

	protected String objectId;
	protected TypeOfEvent eventType;
	private int size;

	public enum TypeOfEvent {
		ALLOCATION, DEATH, UPDATE, METHOD, R, EXCEPTION, OTHER
	}

	public enum Check {
		CREATION, CREATED, NOTBORN, LEGAL, DEAD
	};

	private Check check; // check the category in which the event falls so
							// that it be handled correctly

	public Event(String line) {

		Scanner scanner = new Scanner(line);
		String typeOfEvent = scanner.next();
		eventType = typeConverter(typeOfEvent);

		switch (eventType) {
		case ALLOCATION:
		case DEATH:
		case R:
			objectId = scanner.next();
			break;
		// if it is T or H in ET2
		case EXCEPTION:
			scanner.next();
			scanner.next();
			objectId = scanner.next();
			break;
		case METHOD:
			case UPDATE:
			scanner.next();
			objectId = scanner.next();
			break;
			
		case OTHER : default: break;
		}
		if (eventType == TypeOfEvent.ALLOCATION) {
			String s = scanner.next();
			size = Integer.parseInt(s.trim(), 16);

		} else {

			size = 0;
		}
		scanner.close();
	}

	public Event(Event e) {
		objectId = e.getObjectID();
		eventType = e.getTypeOfEvent();

	}

	public int getSize() {
		return size;
	}

	private TypeOfEvent typeConverter(String s) {
		if (s.equalsIgnoreCase("a") || s.equalsIgnoreCase("i")
				|| s.equalsIgnoreCase("n") || s.equalsIgnoreCase("v")
				|| s.equalsIgnoreCase("p")) {
			return TypeOfEvent.ALLOCATION;
		} else if (s.equalsIgnoreCase("u")) {
			return TypeOfEvent.UPDATE;

		} else if (s.equalsIgnoreCase("d")) {
			return TypeOfEvent.DEATH;

		} else if (s.equalsIgnoreCase("t") || s.equalsIgnoreCase("h")) {
			return TypeOfEvent.EXCEPTION;
		} else if (s.equalsIgnoreCase("r")) {
			return TypeOfEvent.R;
		} else if(s.equalsIgnoreCase("m")||s.equalsIgnoreCase("e")||s.equalsIgnoreCase("x")){
			return TypeOfEvent.METHOD;
		}
		else return TypeOfEvent.OTHER;
	}

	public String getObjectID() {
		return objectId;
	}

	public TypeOfEvent getTypeOfEvent() {
		return eventType;
	}

	public Check getCheck() {
		return check;
	}

	public void setCheck(Check ch) {
		check = ch;
	}

}
