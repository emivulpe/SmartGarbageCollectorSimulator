package uk.ac.glasgow.etparser.handlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import uk.ac.glasgow.etparser.ObjectClass;

public class SmartHeapLeastRecentlyUsed extends SmartHeap {
	public SmartHeapLeastRecentlyUsed() {
		super();
		System.out.println("You created a new LRUHeap");
	}

	protected void deallocate() {
		// create a list of objects ordered by the time of last access
		List<ObjectClass> timeOrderedObjects = getListOfObjectClassTimeSorted();
		while (!sizeNormal() && timeOrderedObjects.size() > 0) {

			// take the least recently used object and remove it from the list
			String currentObjectID = timeOrderedObjects.remove(0).getID();
			// kill that object in the ever seen so
			// it would be treated as a dead object from now on
			killObject(currentObjectID);
			deallocate(currentObjectID);

			// System.out.println("Deallocate "+currentObjectID);
		}
		// System.out.println("least");

	}

	/**
	 * Method the takes all objects from the heap and orders them into a list of
	 * objects sorted by their time of last access. Good if we want to dispose
	 * of least recently used objects.
	 * 
	 * @return list of all objects sorted according to the time of last access
	 */

	public List<ObjectClass> getListOfObjectClassTimeSorted() {
		HashMap<String, ObjectClass> objects = getObjectStates();
		ArrayList<ObjectClass> listOfObjects = new ArrayList<ObjectClass>();
		for (ObjectClass obj : objects.values()) {
			listOfObjects.add(obj);

		}

		Collections.sort(listOfObjects, new Comparator<ObjectClass>() {
			public int compare(ObjectClass o2, ObjectClass o1) {
				return Integer.compare(o2.getTimeOfLastEvent(),
						o1.getTimeOfLastEvent());

			}
		});

		return listOfObjects;
	}

}
