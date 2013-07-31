package uk.ac.glasgow.etparser.handlers;

import java.util.LinkedList;
import java.util.Queue;
import uk.ac.glasgow.etparser.ObjectLiveTime;
import uk.ac.glasgow.etparser.events.Event;
import uk.ac.glasgow.etparser.events.Event.Check;
import uk.ac.glasgow.etparser.events.Event.TypeOfEvent;

public abstract class SmartHeap extends Heap {
	protected Queue<String> allocatedObjects;

	public SmartHeap() {
		super();
		allocatedObjects = new LinkedList<String>();
	}

	protected boolean checkSizeLimitExcess() {
		return livesize >= 30;
	}

	protected boolean sizeNormal() {
		return livesize < 27 && livesize >= 0;
	}

	// the same handle method as in Heap, no deallocations this time
	// I think there should be deallocations + the new heuristic
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
				// System.out.println("first allocation of " + currentObjectID);
				// check for memory excess
				if (checkSizeLimitExcess()) {
					deallocate();

				}

			}
			// if the event isn't allocation
			// report for notborn error
			else {
				// System.out.println("not born because not allocated "
				// + currentObjectID);
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
					System.out
							.println("first allocation of " + currentObjectID);
					// check for memory excess
					if (checkSizeLimitExcess()) {
						deallocate();

					}

				}
				// if the event isn't allocation report not born error
				else {
					// System.out.println("not born because not allocated2 "
					// + currentObjectID);
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
					// System.out.println("dead " + currentObjectID);
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
						// System.out.println(livesize + " livesize, " +
						// allocatedMemSize+" allocated memory");
					} else {
						updateObject(e);
						// System.out.println("legal access " +
						// currentObjectID);
					}

				}

			}

		}
		chart.updateChart(timeSequence, livesize);
	}

	// must be overridden by subclasses
	protected abstract void deallocate();

}
