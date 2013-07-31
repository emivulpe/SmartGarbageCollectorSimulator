package uk.ac.glasgow.etparser;

import static org.junit.Assert.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.glasgow.etparser.events.CreationEvent;
import uk.ac.glasgow.etparser.handlers.EventHandler;
import uk.ac.glasgow.etparser.handlers.EventReporters.EventReport;
import uk.ac.glasgow.etparser.handlers.Heap;

public class ETParserTest {

	private static ETParser parser;
	private static InputStream is;

	@BeforeClass
	public static void setUp() throws Exception {
		System.out.println("file: ");
		String f=new Scanner(System.in).nextLine();
		is = new FileInputStream(f);
		parser = new ETParser(is,new Heap());
	}

	@Test
	public void testConstructor() {
		ETParser et = new ETParser(is,new Heap());
		assertEquals(0, et.getLines());
		assertTrue(et.getHandlers() != null);
		assertTrue(ETParser.getTheHeap() != null);

	}

	@Test
	public void testProcessFile() {
		parser.processFile();
		assertEquals(13, parser.getLines());
	}

	@Test
	public void testInitialiseHandlers() {
		assertEquals(parser.getHandlers().size(), 8);
	}

	@Test
	public void testRegister() {
		parser.registerHandler(new EventHandlerTester());
		assertEquals(parser.getHandlers().size(),8);
	}

	@Test
	public void testNotifyHandlers() {
		CreationEvent e = new CreationEvent("s wt 44 10a");
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
