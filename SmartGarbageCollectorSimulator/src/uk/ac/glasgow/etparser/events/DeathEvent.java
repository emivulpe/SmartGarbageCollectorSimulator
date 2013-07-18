package uk.ac.glasgow.etparser.events;

public class DeathEvent extends Event {

	public DeathEvent(String line){
		super(line);
		status="D";
	}
}
