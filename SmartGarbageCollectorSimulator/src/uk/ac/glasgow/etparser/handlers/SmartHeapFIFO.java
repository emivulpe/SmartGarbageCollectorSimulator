package uk.ac.glasgow.etparser.handlers;

public class SmartHeapFIFO extends SmartHeap {

	public SmartHeapFIFO() {
		super();
		System.out.println("You created a new FIFOHeap");
	}

	protected void deallocate() {
		while ((!sizeNormal())) {
			// get the id of the first allocated object
			// and remove it from the list of allocated objects
			String currentObjectID = allocatedObjects.remove(0);
//			System.out.println(allocatedMemSize+" memory");
//			System.out.println("delete "+currentObjectID);
			// kill that object in the ever seen so
			// it would be treated as a dead object from now on
			killObject(currentObjectID);
			deallocate(currentObjectID);

			// System.out.println("first");
		}
	}
}
