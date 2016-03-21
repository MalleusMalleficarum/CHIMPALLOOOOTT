package edu.kit.ipd.creativecrowd.readablemodel;

// TODO: Auto-generated Javadoc
/**
 * read-only information about the experiment specification.
 *
 * @author simon
 */
public interface ExperimentSpec extends AbstractExperiment {

	/**
	 * Gets the rating options.
	 *
	 * @return the rating options
	 */
	public Iterable<RatingOption> getRatingOptions();

	/**
	 * Gets the picture.
	 *
	 * @return the picture
	 */
	public String getPicture();

	/**
	 * Gets the picture license.
	 *
	 * @return the picture license
	 */
	public String getPictureLicense();

	/**
	 * Gets the question.
	 *
	 * @return the question
	 */
	public String getQuestion();

	/**
	 * Gets the rating task question.
	 *
	 * @return the question
	 */
	public String getRatingTaskQuestion();

}
