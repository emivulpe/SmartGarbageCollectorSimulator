package uk.ac.glasgow.etparser.handlers;

import java.util.ArrayList;
import java.util.List;
import uk.ac.glasgow.etparser.ObjectLiveTime;
import uk.ac.glasgow.etparser.events.Event;
import uk.ac.glasgow.etparser.events.Event.Check;
import uk.ac.glasgow.etparser.events.Event.TypeOfEvent;

public abstract class SmartHeap extends Heap {

	protected int threshold; // default would be 70000 for now unless
									// otherwise specified

	private double percentageToDeallocate; // default would be 20% if not
													// otherwise specified

	protected List<String> allocatedObjects;

	public SmartHeap() {
		super();
		allocatedObjects = new ArrayList<String>();
		threshold = 30;
		percentageToDeallocate = 0.2;

	}

	public void specifyThreshold(int t) {
		threshold = t;
		System.out.println("threshold is " + threshold);

	}

	public void specifyPercentageToDeallocate(int p) {
		percentageToDeallocate = p / 100.0;
		System.out.println("percentage is " + percentageToDeallocate);
	}

	protected boolean checkSizeLimitExcess() {
		return allocatedMemSize >= threshold;
	}

	protected boolean sizeNormal() {
		return allocatedMemSize <= threshold-(percentageToDeallocate * threshold) && allocatedMemSize >= 0;
	}


	@Override
	public void handle(Event e) {
		timeSequence++;
		String currentObjectID = e.getObjectID();
		TypeOfEvent currentEventType = e.getTypeOfEvent();
		// if never seen before
		if (!existsInEverSeen(currentObjectID)) {
			// create a new livetime for this object
			ObjectLiveTime livetime = new ObjectLiveTime(currentObjectID);
			everSeen.put(currentObjectID, livetime);
			// if the event is allocation- great
			if (currentEventType == TypeOfEvent.ALLOCATION) {
				livetime.giveBirth();
				allocateObject(e);
				allocatedObjects.add(currentObjectID);
				chart.updateChart(timeSequence, livesize);
//				 System.out.println("first allocation of " + currentObjectID);
				 //check for memory excess
				if (checkSizeLimitExcess()) {
					deallocate();

				}
				chart.updateChart(timeSequence, livesize);

			}
			// if the event isn't allocation
			// report for notborn error
			else {
//				 System.out.println("not born because not allocated "
//				 + currentObjectID);
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
				if (currentEventType == TypeOfEvent.ALLOCATION) {
					everSeen.get(currentObjectID).giveBirth();
					allocateObject(e);
					allocatedObjects.add(currentObjectID);
//					System.out
//							.println("first allocation of " + currentObjectID);
					chart.updateChart(timeSequence, livesize);
					// check for memory excess
					if (checkSizeLimitExcess()) {
						deallocate();
						
						

					}
					chart.updateChart(timeSequence, livesize);
					

				}
				// if the event isn't allocation report not born error
				else {
//					 System.out.println("not born because not allocated2 "
//					 + currentObjectID);
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
//					System.out
//							.println("not live but exists- either preaccess or it was dead "
//									+ currentObjectID);
					e.setCheck(Check.NOTBORN);
				}
				// the object died before this access
				else if (currentObjectLivetime.isDead()) {
					e.setCheck(Check.DEAD);
//					 System.out.println("dead " + currentObjectID);
				}
				// it's legal to update this object
				else {
					// if the event isn't dead just make the update
					e.setCheck(Check.LEGAL);

					// if it was a death event kill the object in everLived,
					// deallocate it from the heap
					if (currentEventType == TypeOfEvent.DEATH) {

						killObject(currentObjectID);
						chart.updateChart(timeSequence, livesize);
//						 System.out.println(livesize + " livesize, " +
//						 allocatedMemSize+" allocated memory");
					} else {
						updateObject(e);
//						 System.out.println("legal access " +
//						 currentObjectID);
					}

				}

			}

		}
		if (timeSequence % EVENTSINTERVAL == 0)
			chart.updateChart(timeSequence, livesize);
	}

	// must be overridden by subclasses
	protected abstract void deallocate();

}
