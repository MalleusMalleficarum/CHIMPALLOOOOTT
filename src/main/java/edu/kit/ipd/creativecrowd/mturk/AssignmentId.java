package edu.kit.ipd.creativecrowd.mturk;

/**
 * @author Tobias  H 
 * An assignmentId is the unique Id of an Assignment which
 * gets edited by a worker
 */
public class AssignmentId {
	private String Id;
	/**
	 * @param Id The Id of the assignment
	 */
	public AssignmentId(String Id)
	{
		setId(Id);
	}
	/**
	 * @return the Id of the assignment
	 */
	public String getId() {
		return Id;
	}

	/**
	 * @param id The Id of the assignment
	 */
	public void setId(String id) {
		Id = id;
	}
}