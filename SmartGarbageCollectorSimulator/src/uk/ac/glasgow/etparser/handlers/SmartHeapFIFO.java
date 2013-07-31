package uk.ac.glasgow.etparser.handlers;


public class SmartHeapFIFO extends SmartHeap{
	
	
	
	public SmartHeapFIFO(){
		super();
		System.out.println("You created a new FIFOHeap");
	}

	
	
	protected void deallocate() {
		while (checkSizeLimitExcess() && (!sizeNormal())) {
			//get the id ot the first allocated object
			//and remove it from the list of allocated objects
			String currentObjectID = allocatedObjects.remove();
			//get the size or the first allocated object
			//and remove it from the heap
			int sizeOfObject = memory.remove(currentObjectID).getSize();
			System.out.println(sizeOfObject+ " size");
			//kill that object in the ever seen so
			//it would be treated as a dead object from now on
			System.out.println(everSeen.get(currentObjectID));
			everSeen.get(currentObjectID).kill();
			//decrease the allocated memory size
			allocatedMemSize -= sizeOfObject;
			//decrease the livesize
			livesize -= sizeOfObject;
			everSeen.get(currentObjectID).kill();
		}
		System.out.println("first");
	}
}
