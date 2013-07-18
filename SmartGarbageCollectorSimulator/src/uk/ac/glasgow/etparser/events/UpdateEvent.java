package uk.ac.glasgow.etparser.events;

public class UpdateEvent extends Event{
	
	public UpdateEvent (String line){
		super(line);
		status = "U";
	}

}
