package uk.ac.glasgow.etparser.handlers;

import java.util.HashMap;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import uk.ac.glasgow.etparser.ObjectClass;
import uk.ac.glasgow.etparser.CommandParser.WayToDealWithErrors;
import uk.ac.glasgow.etparser.ObjectLiveTime;
import uk.ac.glasgow.etparser.events.CreationEvent;
import uk.ac.glasgow.etparser.events.Event;
import uk.ac.glasgow.etparser.events.Event.Check;
import uk.ac.glasgow.etparser.events.Event.TypeOfEvent;

public class Heap implements EventHandler {

	/**
	 * Measures time sequentially (1, 2, 3...).
	 */
	protected int timeSequence;
	/**
	 * Measures time as function of the allocated objects' sizes.
	 */
	protected int timeSize;

	/**
	 * Measures time in terms of method entry and method exit.
	 */
	protected int timeMethod;
	/**
	 * A hash map that keeps the object id and the last event that happened to
	 * that object.
	 */
	protected HashMap<String, ObjectClass> memory;

	protected HashMap<String, ObjectLiveTime> everSeen;

	protected int livesize, allocatedMemSize;

	protected static LiveSizeChart chart;

	protected WayToDealWithErrors dealWithPreaccess, dealWithPostAccess;

	/**
	 * Initializes the class variables. Private because of Singleton design
	 * pattern.
	 */
	public Heap() {
		chart = new LiveSizeChart();
		livesize = 0;
		allocatedMemSize = 0;
		timeSequence = 0;
		timeSize = 0;
		timeMethod = 0;
		memory = new HashMap<String, ObjectClass>();
		everSeen = new HashMap<String, ObjectLiveTime>();
		System.out.println("You created a new Heap");

	}

	public void setDealWithPreaccess(WayToDealWithErrors wayToDeal) {

		dealWithPreaccess = wayToDeal;

	}

	public void setDealWithPostaccess(WayToDealWithErrors wayToDeal) {
		dealWithPostAccess = wayToDeal;

	}

	public static void createChart() {
		chart.setVisible(true);

	}

	/**
	 * Checks whether the event is legal and if it is, it updates the event
	 * record in the heap and updates the time.
	 * 
	 * @param e
	 *            instance of the Event super class
	 */

	public void handle(Event e) {

		timeSequence++;
		System.out.println(timeSequence+" Time");
		String currentObjectID = e.getObjectID();
		TypeOfEvent currentEventType = e.getTypeOfEvent();
		// if never seen before
		if (!existsInEverSeen(currentObjectID)) {

			// create a new livetime for this object
			ObjectLiveTime livetime = new ObjectLiveTime(currentObjectID);
			everSeen.put(currentObjectID, livetime);

			// if the event is allocation- great
			if (currentEventType==TypeOfEvent.ALLOCATION) {
				livetime.giveBirth();
				allocateObject((CreationEvent) e);
				chart.updateChart(timeSequence, livesize);
				System.out.println("first allocation of " + currentObjectID);

			}
			// if the event isn't allocation
			// report for notborn error
			else {
				System.out.println("not born because not allocated "
						+ currentObjectID);
				e.setCheck(Check.NOTBORN);
			}
			everSeen.put(currentObjectID, livetime);

		}

		// the object has been seen before
		else {
			// if it wasn't allocated (check whether it has been deallocated
			if (!existsInHeap(currentObjectID)
					&& !everSeen.get(currentObjectID).isDead()) {
				// if the event is allocation- perfect
				if (currentEventType==TypeOfEvent.ALLOCATION) {
					everSeen.get(currentObjectID).giveBirth();
					allocateObject((CreationEvent) e);
					chart.updateChart(timeSequence, livesize);
					System.out
							.println("first allocation of " + currentObjectID);

				}
				// if the event isn't allocation report not born error
				else {
					System.out.println("not born because not allocated2 "
							+ currentObjectID);
					e.setCheck(Check.NOTBORN);

				}

			}
			// it has been allocated
			else {

				// take the object and check it's livetime
				ObjectLiveTime currentObjectLivetime = everSeen
						.get(currentObjectID);
				// if it was never born, probably preaccess before and now again
				// or probably dead
				if (!currentObjectLivetime.isBorn()) {
					System.out
							.println("not live but exists- either preaccess or it was dead "
									+ currentObjectID);
					e.setCheck(Check.NOTBORN);
				}
				// the object died before this access
				else if (currentObjectLivetime.isDead()) {
					e.setCheck(Check.DEAD);
					System.out.println("dead " + currentObjectID);
				}
				// it's legal to update this object
				else {
					// if the event isn't dead just make the update
					e.setCheck(Check.LEGAL);

					// if it was a death event kill the object in everLived,
					// deallocate it from the heap
					if (currentEventType==TypeOfEvent.DEATH) {

						killObject(currentObjectID);
						deallocate(currentObjectID);
						chart.updateChart(timeSequence, livesize);

						System.out.println("kill " + currentObjectID);
					} else {
						updateObject(e);
						System.out.println("legal access " + currentObjectID);
					}

				}

			}

		}
	}

	protected void deallocate(String objectID) {
		ObjectClass remove = memory.remove(objectID);
		allocatedMemSize -= remove.getSize();
	}

	protected void killObject(String objectID) {
		everSeen.get(objectID).kill();
		livesize -= memory.get(objectID).getSize();
	}

	protected boolean existsInHeap(String objectID) {
		return memory.get(objectID) != null;
	}

	protected boolean existsInEverSeen(String objectID) {
		return everSeen.get(objectID) != null;
	}

	protected void allocateObject(CreationEvent e) {
		String currentObjectID = e.getObjectID();
		int size = e.getSize();
		livesize += size;
		allocatedMemSize += size;
		ObjectClass object = new ObjectClass(size, timeSequence,
				currentObjectID);
		everSeen.get(currentObjectID).giveBirth();
		// ObjectEventRecord record = new ObjectEventRecord(e);
		memory.put(currentObjectID, object);
		e.setCheck(Check.CREATION);
		CreationEvent ce = (CreationEvent) e;
		timeSize += ce.getSize();

	}

	protected void allocateObjectCheater(Event e) {
		String currentObjectID = e.getObjectID();
		ObjectClass object = new ObjectClass(0, timeSequence, currentObjectID);
		everSeen.get(currentObjectID).giveBirth();
		// ObjectEventRecord record = new ObjectEventRecord(e);
		memory.put(currentObjectID, object);
		e.setCheck(Check.CREATION);
		CreationEvent ce = new CreationEvent(e);
		timeSize += ce.getSize();

	}

	protected void updateObject(Event e) {
		String currentObjectID = e.getObjectID();
		ObjectClass accessedObject = memory.get(currentObjectID);
		if (accessedObject != null) {
			TypeOfEvent currentEventStatus = e.getTypeOfEvent();
			accessedObject.updateEvent(timeSequence, e.getTypeOfEvent());
			memory.put(currentObjectID, accessedObject);
			e.setCheck(Check.LEGAL);
			if (currentEventStatus==TypeOfEvent.OTHER) {
				timeMethod++;
			}

		}

	}

	protected void decisionMakerPreaccess(Event e) {

		switch (dealWithPreaccess) {
		case MOVE:
			handleAllocateAtFirstAccess(e);
			break;
		default:
			handleIgnorePreaccess();

		}
	}

	protected void decisionMakerPostaccess() {
		switch (dealWithPostAccess) {
		case MOVE:
			handleMoveDeath();
			break;
		default:
			handleIgnorePostaccess();

		}
	}

	/**
	 * 
	 * @return the number of objects tried to be accessed ever.
	 */
	public int getNumObjects() {
		System.out.println("number in heap " + everSeen.size());
		return everSeen.size();
	}

	/**
	 * 
	 * @param oid
	 *            the id of the object we want to access in the heap.
	 * @return the last event record for the given object.
	 */
	public ObjectClass getRecord(String oid) {
		return memory.get(oid);
	}

	/**
	 * 
	 * @return the current time expressed as sequence.
	 */
	public int getTimeSequence() {
		return timeSequence;
	}

	/**
	 * 
	 * @return the current time expressed as allocated object size.
	 */
	public int getTimeSize() {
		return timeSize;
	}

	/**
	 * 
	 * @return the current time expressed in term of method entry and exit.
	 */
	public int getTimeMethod() {
		return timeMethod;
	}

	// getters

	public long getLiveSize() {
		return livesize;
	}

	public long getAllocatedMemSize() {
		return allocatedMemSize;
	}

	protected void handleIgnorePreaccess() {
		System.out.println("ignore preaccess");
		// as this method just ignores preaccess it doesn't do anything

	}

	protected void handleAllocateAtFirstAccess(Event e) {
		System.out.println("allocate at first access");
		allocateObjectCheater(e);
		updateObject(e);

	}

	protected void handleIgnorePostaccess() {
		System.out.println("ignore postaccess");
		// as this method just ignores postaccess it doesn't do anything

	}

	// no need for this in my opinion. we don't need an object to be dead to
	// collect it
	protected void handleMoveDeath() {
		System.out.println("move death");

	}

	public void removeRecord(String objectID) {
		memory.remove(objectID);

	}

	public HashMap<String, ObjectClass> getObjectStates() {

		return memory;
	}

	public class LiveSizeChart extends ApplicationFrame {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private XYSeries data;

		public LiveSizeChart() {

			super("Live size chart");

			data = new XYSeries("Data");
			data.add(0, 0);

			XYSeriesCollection dataset = new XYSeriesCollection(data);

			// based on the dataset we create the chart
			JFreeChart chart = ChartFactory.createXYLineChart(
					"Livesize change over time", "Time", "Livesize", dataset,
					PlotOrientation.VERTICAL, false, false, false);

			// we put the chart into a panel
			ChartPanel chartPanel = new ChartPanel(chart);

			// default size
			chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));

			// add it to our application
			setContentPane(chartPanel);
			pack();

		}

		public void updateChart(double x, double y) {
			data.add(x, y);
		}

	}

}
