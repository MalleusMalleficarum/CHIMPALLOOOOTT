package edu.kit.ipd.creativecrowd.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.RatingOption;
import edu.kit.ipd.creativecrowd.readablemodel.ExperimentSpec;

public class ExperimentSpecFromConfig implements ExperimentSpec{
	private int budgetCents;
	private int basicPaymentHITCents;
	private int basicPaymentAnswerCents;
	private int bonusPaymentCents;
	private int basicPaymentRatingCents;
	private int maxNumAns;
	private int maxNumRat;
	private String picture;
	private String pictureLicense;
	private String id;
	private String description;
	private String question;
	private String HITTitle;
	private String hitDescription;
	private String ratingTaskViewClass;
	private String accordingRatingTaskDescription;
	private List<RatingOption> ratingOptions;
	private Map<String, String> strategyParams;
	private Iterable<String> qualifications;
	private Iterable<String> tags; 
	
	/**
	 * initiates an ExperimentSpec
	 * @param budgetCents				How big is the total budget for the experiment(in US Cents)
	 * @param basicPaymentHITCents		How much money is paid for each submitted HIT 
	 * @param basicPaymentAnswerCents	How much money is paid for each given answer
	 * @param bonusPaymentCents			the amount of the bonus payment
	 * @param basicPaymentRatingCents	how much money is paid per rating
	 * @param picture					path to the picture
	 * @param pictureLicense			path to the picturelicense
	 * @param id						the id/name of the experiment
	 * @param description				the description of the creative task
	 * @param ratingTaskDescription		the description of the rating task according to the creative task
	 * @param ratingOps					a map of ratingoptions
	 * @param strategyParams			map of strategy parameters
	 * @param qualifications			the qualifications necessary for the mturk workers
	 * @param tags						tags the experiment should have on mturk
	 */
	public ExperimentSpecFromConfig(int budgetCents, int basicPaymentHITCents, int basicPaymentAnswerCents, int bonusPaymentCents, int basicPaymentRatingCents,
			String question,String ratingTaskDescription, String picture, String pictureLicense, String id, String description, String ratingTaskViewClass, Map<String, Double> ratingOps,
			Map<String, String> strategyParams, Iterable<String> qualifications, Iterable<String> tags, String hitTi, String hitDesc, int maxNumAns, int maxNumRat) {
		this.budgetCents = budgetCents;
		this.basicPaymentHITCents = basicPaymentHITCents;
		this.basicPaymentAnswerCents = basicPaymentAnswerCents;
		this.bonusPaymentCents = bonusPaymentCents;
		this.question = question;
		this.picture = picture;
		this.pictureLicense = pictureLicense;
		this.id = id;
		this.description = description;
		this.accordingRatingTaskDescription = ratingTaskDescription;
		ratingOptions = new LinkedList<RatingOption>();
		for (Map.Entry<String, Double> entry : ratingOps.entrySet()){
			Float d =((Double)entry.getValue()).floatValue();
			RatingOption op = new RatingOptionSpec(entry.getKey(), d);
			ratingOptions.add(op);
		}
		this.strategyParams = strategyParams;
		this.qualifications = qualifications;
		this.tags = tags;
		this.basicPaymentRatingCents = basicPaymentRatingCents;
		this.HITTitle = hitTi;
		this.hitDescription = hitDesc;
		this.maxNumAns = maxNumAns;
		this.maxNumRat = maxNumRat;
		this.ratingTaskViewClass = ratingTaskViewClass;
	}
	/**
	 * 
	 * @return	the initial budget
	 */
	  public int getBudgetCents() {
		return budgetCents;
	}
	  /**
	   * 
	   * @return	the basic payment for a HIT
	   */
	  public int getBasicPaymentHITCents() {
		return basicPaymentHITCents;
	}
	  /**
	   * 
	   * @return	the basic payment for an answer
	   */
	  public int getBasicPaymentAnswerCents() {
		return basicPaymentAnswerCents;
	}
	  /**
	   * 
	   * @return	the basic payment for a rating
	   */
	  public int getBasicPaymentRatingCents() {
		return basicPaymentRatingCents;
	}
	  /**
	   * 
	   * @return	the amount of the bonus payment
	   */
	  public int getBonusPaymentCents() {
		return bonusPaymentCents;
	}
	  /**
	   * 
	   * @return	an Iterable containing all RatingOptions
	   */
	  public Iterable<RatingOption> getRatingOptions() {
		return ratingOptions;
	}
	  /**
	   * 
	   * @return	the path to the picture to be used in the CreativeTask
	   */
	  public String getPicture() {
		return picture;
	}
	  /**
	   * 
	   * @return	the path to the picture license
	   */
	  public String getPictureLicense() {
		return pictureLicense;
	}
	  /**
	   * 
	   * @return	the experiment ID
	   */
	  public String getId() {
		return id;
	} 
	  /**
	   * 
	   * @return	a Map of all strategy parameters
	   */
	  public Map<String, String> getStrategyParams() {
		return strategyParams;
	}
	  /**
	   * @return an Iterable containing all qualifications
	   */
	  public Iterable<String> getQualifications() {
		return qualifications;
	}
	@Override
	public String getDescription() {
		return description;
	}
	@Override
	public Iterable<String> getTags() {
		return tags;
	}

	@Override
	public String getQuestion() {
		// TODO Auto-generated method stub
		return question;
	}
	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return id;
	}
	@Override
	public String getHITTitle() throws DatabaseException {
		// TODO Auto-generated method stub
		return HITTitle;
	}
	@Override
	public String getHITDescription() throws DatabaseException {
		// TODO Auto-generated method stub
		return hitDescription;
	}
	@Override
	public int getMaxNumberOfAnswersPerAssignment() throws DatabaseException {
		// TODO Auto-generated method stub
		System.out.println("maxNum:"+maxNumAns);
		return maxNumAns;
	}
	@Override
	public int getMaxNumberOfRatingsPerAssignment() throws DatabaseException {
		// TODO Auto-generated method stub
		return maxNumRat;
	}
	@Override
	public String getRatingTaskViewClass() throws DatabaseException {
		// TODO Auto-generated method stub
		return ratingTaskViewClass;
	}

	@Override
	public String getRatingTaskQuestion() {
		return accordingRatingTaskDescription;
	}

}
