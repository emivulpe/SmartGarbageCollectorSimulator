package uk.ac.glasgow.etparser;

import java.util.Scanner;

import uk.ac.glasgow.etparser.events.*;

public class EventFactory {

	public Event createEvent(String line) {
		Event e = null;
		Scanner scan = new Scanner(line);
		String type = scan.next();
		if (type.equalsIgnoreCase("A")) {
			e = new CreationEvent(line);
		}

		else if (type.equalsIgnoreCase("U")) {
			e = new UpdateEvent(line);
		}

		else if (type.equalsIgnoreCase("D")) {
			e = new DeathEvent(line);
		}


		else {
			e = new UninterestingEvent(line);
		}

		scan.close();
		return e;
	}
}
