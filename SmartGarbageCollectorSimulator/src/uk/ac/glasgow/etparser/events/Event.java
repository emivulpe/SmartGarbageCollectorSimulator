package uk.ac.glasgow.etparser.events;
import java.util.Scanner;

public abstract class Event {

	protected String objectId;
	protected String allocationTime;
	protected String status;


	// check must probably be enum as well
	private String check = ""; // check the category in which the event falls so
								// that it be handled correctly


	public Event(String line) {

		Scanner scanner = new Scanner(line);
		String status=scanner.next();
		allocationTime = scanner.next();
		if (status.equalsIgnoreCase("A") || status.equalsIgnoreCase("R")
				|| status.equalsIgnoreCase("D")) {
			objectId = scanner.next();
		} else {
			scanner.next();
			objectId = scanner.next();
		}

		scanner.close();
	}
	
	public Event (Event e){
		objectId=e.getObjectID();
		allocationTime=e.getAllocationTime();
		status=e.getStatus();
		
	}

	public String getObjectID() {
		return objectId;
	}

	public String getAllocationTime() {
		return allocationTime;
	}


	public String getStatus(){
		return status;
	}

	public String getCheck() {
		return check;
	}

	public void setCheck(String s) {
		check = s;
	}
	



}
