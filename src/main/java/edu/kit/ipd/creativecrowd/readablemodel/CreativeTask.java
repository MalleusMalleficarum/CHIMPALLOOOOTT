package edu.kit.ipd.creativecrowd.readablemodel;

import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

/**
 * provides read-only methods about a creative task. A CreativeTask represents any task where a Worker has to respond with creative content.
 * Can contain besides - the task description - picture (see picture url and picture license url)
 *
 * @author simon
 */
public interface CreativeTask extends Task {

	/**
	 * Gets the answers which replied to this CreativeTask
	 *
	 * @return all answers replying to this task
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public Iterable<? extends Answer> getAnswers() throws DatabaseException;

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.Task#getID()
	 */
	public String getID();

	/**
	 * Gets the URL which points to the location where the optional picture is shown
	 *
	 * @return th url to the picture
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public String getPictureURL() throws DatabaseException;

	/**
	 * Gets the url which points to the location where the chosen picture is seen in context (the legal "source")
	 *
	 * @return the picture license url
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public String getPictureLicenseURL() throws DatabaseException;

	/**
	 * Gets the text describing the problem this task searches answers for
	 *
	 * @return the description as String
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public String getDescription() throws DatabaseException;

	/**
	 * Gets the text describing text of the description of an according rating task
	 *
	 * @return the according description as String
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public String getAccordingRatingTaskDescription() throws DatabaseException;
}
