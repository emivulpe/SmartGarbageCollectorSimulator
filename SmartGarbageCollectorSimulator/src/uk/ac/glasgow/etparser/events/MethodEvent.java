package uk.ac.glasgow.etparser.events;

public class MethodEvent extends Event {

	public MethodEvent(String line) {
		super(line);
		status="M";
	}

}
