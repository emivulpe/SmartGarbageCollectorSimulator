package uk.ac.glasgow.etparser;

import static org.junit.Assert.*;
import java.io.FileInputStream;
import java.io.InputStream;
import org.junit.Before;
import org.junit.Test;
import uk.ac.glasgow.etparser.events.CreationEvent;
import uk.ac.glasgow.etparser.handlers.EventHandler;
import uk.ac.glasgow.etparser.handlers.EventReport;
import uk.ac.glasgow.etparser.handlers.SimulatedHeap;

public class ETParserTest {

	private ETParser parser;
	private InputStream is;

	@Before
	public void setUp() throws Exception {
		is = new FileInputStream("C:/Users/Emi/Desktop/traces/beforeborn.txt");
		parser = new ETParser(is);
	}

	@Test
	public void testConstructor() {
		ETParser et = new ETParser(is);
		assertEquals(0, et.getLines());
		assertTrue(et.getHandlers() != null);
		assertTrue(SimulatedHeap.getTheHeap() != null);

	}

	@Test
	public void testProcessFile() {
		parser.processFile();
		assertEquals(5, parser.getLines());
	}

	@Test
	public void testInitialiseHandlers() {
		assertEquals(parser.getHandlers().size(), 8);
	}

	@Test
	public void testRegister() {
		parser.registerHandler(new EventHandlerTester());
		assertEquals(parser.getHandlers().size(), 9);
	}

	@Test
	public void testNotifyHandlers() {
		CreationEvent e = new CreationEvent("s wt hye4 10a");
		EventHandlerTester tester = new EventHandlerTester();
		parser.registerHandler(tester);
		parser.notifyHandlers(e);
		assertTrue(tester.getHandled());
	}

	@Test
	public void testPrintReport() {
		EventHandlerTester tester = new EventHandlerTester();
		parser.registerHandler(tester);
		parser.printReport();
		assertEquals(tester.getReport(), "well done!");
	}

	public class EventHandlerTester implements EventHandler, EventReport {
		public boolean handled;
		public String report;

		public EventHandlerTester() {
			handled = false;
			report = "";
		}

		public boolean getHandled() {
			return handled;
		}

		public String getReport() {
			return report;
		}

		@Override
		public String finalReport() {
			report = "well done!";
			return report;
		}

		@Override
		public void handle(uk.ac.glasgow.etparser.events.Event e) {
			handled = true;

		}

	}

}

