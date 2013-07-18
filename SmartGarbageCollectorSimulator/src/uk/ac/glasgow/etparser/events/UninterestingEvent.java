package uk.ac.glasgow.etparser.events;

public class UninterestingEvent extends Event {
	
	public UninterestingEvent(String line){
		super(line);
		status = "O";
		
	}

}
