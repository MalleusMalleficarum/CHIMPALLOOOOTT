package edu.kit.ipd.creativecrowd.connector;

/**
 * specification of a rating given by a user
 * @author simon
 *
 */
public interface RatingSpec {
	/**
	 * get the Id of the Answer this rating was given for
	 * @return the Id
	 */
	String getAnswerID();
	
	/**
	 * get the Id of the chosen ratingOption
	 * @return the Id
	 */
	String getRatingOptionId();
	
	/**
	 * get the text that was given with the rating
	 * 
	 * @return the text
	 */
	String getText();
}
