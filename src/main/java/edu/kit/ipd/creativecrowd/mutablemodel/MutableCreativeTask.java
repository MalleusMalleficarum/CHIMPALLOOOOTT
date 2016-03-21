package edu.kit.ipd.creativecrowd.mutablemodel;

import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.*;

/**
 * adds setter-functionality to the CreativeTask class.
 * 
 * @see edu.kit.ipd.creativecrowd.readablemodel.CreativeTask
 * @author simon
 */
public interface MutableCreativeTask extends CreativeTask {

	/**
	 * Sets the description(question).
	 *
	 * @param description the new description
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void setDescription(String description) throws DatabaseException;

	/**
	 * Sets the picture.
	 *
	 * @param urlImage the url image
	 * @param urlLicense the url license
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void setPicture(String urlImage, String urlLicense) throws DatabaseException;

	/*-?|Test Repo-Review|Philipp|c2|?*/

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.CreativeTask#getAnswers()
	 */
	public Iterable<? extends MutableAnswer> getAnswers() throws DatabaseException;

	/**
	 * Sets the description of all according rating tasks(question).
	 *
	 * @param description the new description
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void setAccordingRatingTaskDescription(String description) throws DatabaseException;
}
