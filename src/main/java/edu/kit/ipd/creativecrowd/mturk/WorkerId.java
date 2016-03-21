package edu.kit.ipd.creativecrowd.mturk;

/**
 * @author Tobias H.
 *         This class represents the WorkerId from an amazon MTurk worker
 */
public class WorkerId {
	private String Id;

	/**
	 * @param Id the ID of the worker
	 */
	public WorkerId(String Id)
	{
		setId(Id);
	}

	/**
	 * @return the id of the worker
	 */
	public String getId() {
		return Id;
	}

	/**
	 * @param id the id of the worker to set
	 */
	private void setId(String id) {
		Id = id;
	}

}