package uk.ac.glasgow.etparser;

/**
 * 
 * @author Emi
 * @version 1.0
 * 
 *          This is a class that records the livetime of an object. When we
 *          first see an object, we create it's livetime. We assume that it is
 *          not born at its first occurence as we leave the possibility for
 *          preaccess. Everytime an object changes it's state, the booleans
 *          born, live and dead must be changed accordingly.
 */
public class ObjectLiveTime {


	private String objectID;
	private boolean born;
	private boolean dead;

	public ObjectLiveTime(String id) {
		objectID = id;
		born = false;
		dead = false;
	}

	public void giveBirth() {
		born = true;
	}

	public void kill() {
		dead = true;
	}

	public boolean isBorn() {
		return born;
	}

	public boolean isDead() {
		return dead;
	}
	
	public String getObjectID(){
		return objectID;
	}
}
