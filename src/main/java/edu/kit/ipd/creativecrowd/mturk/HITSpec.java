package edu.kit.ipd.creativecrowd.mturk;

import java.util.Iterator;

import com.amazonaws.mturk.requester.Comparator;
import com.amazonaws.mturk.requester.QualificationRequirement;

/**
 * @author Tobias H A HITSpec is a class containing all information to publish a
 *         hit on Amazon MTurk
 */
public class HITSpec {
	private String title;
	private String description;
	private int rewardCents;
	private String keywords;
	private int completiontime;
	private int expireingtime;
	private QualificationRequirement[] qualifications;
	private String externalQuestion;
	private int numAssignments;
	private int estimateTime;
	private int workerTimeout;

	/**
	 * @param title
	 *            the HIT's title
	 * @param description
	 *            the description of the hit
	 * @param rewardCents
	 *            the basic reward for worker
	 * @param numAssignments
	 *            the number of assignments which get published
	 * @param externalQuestionURL
	 *            the URL where the task is
	 * @param estimate
	 *            the estimated time to complete a hit
	 * @param completiontime
	 *            the time a worker have to complete a hit until it gets
	 *            rejected
	 * @param qualifications
	 *            the qualifications a worker must have to accept a hit
	 */
	public HITSpec( String title, String description,
			int rewardCents, int numAssignments, String externalQuestionURL,
			int estimate, int completiontime, Iterable<String> qualifications) throws IllegalInputException{
		this.setTitle(title);
		this.setDescription(description);
		this.setRewardCents(rewardCents);
		this.setNumAssignments(numAssignments);
		this.setEstimateTime(estimate);
		this.setCompletiontime(completiontime);
		this.setExternalQuestion(externalQuestionURL, 800);
		this.setQualifications(qualifications);
		this.setExpireingtime(2592000);
	}

	/**
	 * @return the title
	 */
	public String getTitle(){
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) throws IllegalInputException{
		if(title==null)
		{
			throw new IllegalInputException("Title must be set.", "title");
		}
		this.title = title;
	}

	/**
	 * Generates an External Question using it's xml representation
	 * 
	 * @param externalQuestionURL
	 *            The URL where the HITTasks are
	 * @param frameHeight
	 *            The height of the frame in which the task is visible
	 */
	public void setExternalQuestion(String externalQuestionURL, int frameHeight) throws IllegalInputException{
		if(frameHeight<=0)
		{
			throw new IllegalInputException("The heigt of the external question frame must be higher than 0", "frameHeight");
		}else if (externalQuestionURL==null)
		{
			throw new IllegalInputException("External QuestionURL must be set.","externalQuestionURL");
		}else if (!externalQuestionURL.contains("https"))
		{
			throw new IllegalInputException("External question URL must use https.","externalQuestionURL");
		}
		this.externalQuestion = "<?xml version=\"1.0\"?>\n<ExternalQuestion xmlns=\"http://mechanicalturk.amazonaws.com/AWSMechanicalTurkDataSchemas/2006-07-14/ExternalQuestion.xsd\">\n<ExternalURL>"
				+ externalQuestionURL
				+ "</ExternalURL>\n<FrameHeight>"
				+ frameHeight + "</FrameHeight>\n</ExternalQuestion>";

	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the rewardCents
	 */
	public int getRewardCents() {
		return rewardCents;
	}

	/**
	 * @param rewardCents
	 *            the rewardCents to set
	 */
	public void setRewardCents(int rewardCents) throws IllegalInputException{
		if(rewardCents < 0)
		{
			throw new IllegalInputException("The reward in Cents must be higher than 0.","rewardCents");
		}
		this.rewardCents = rewardCents;
	}

	/**
	 * @return the keywords
	 */
	public String getKeywords() {
		return keywords;
	}

	/**
	 * converts the keywords to a mturk standart
	 * 
	 * @param keywords
	 *            the keywords to set
	 */
	public void setKeywords(String[] keywords) {
		String keyword = "";
		if (keywords != null && keywords.length > 0) {
			keyword = keywords[0];
			for (int i = 1; i < keywords.length; i++) {
				keyword = keyword + ", " + keywords[i];
			}
		}
		this.keywords = keyword;
	}

	/**
	 * @return the completiontime
	 */
	public int getCompletiontime() {
		return completiontime;
	}
	//TODO completiontime estimatedTime und worker Timeout sind das selbe 2 rauskicken
	/**
	 * @param completiontime
	 *            the completiontime to set
	 */
	public void setCompletiontime(int completiontime) throws IllegalInputException{
		if(completiontime<30||completiontime>31536000)
		{
			throw new IllegalInputException("Completiontime for a worker to complete a hit must be between 30 and 31536000 seconds.", "completiontime");
		}
		this.completiontime = completiontime;
	}

	/**
	 * @return the expireingtime
	 */
	public int getExpireingtime() {
		return expireingtime;
	}

	/**
	 * @param expireingtime
	 *            the expireingtime to set
	 */
	public void setExpireingtime(int expireingtime) throws IllegalInputException{
		if(expireingtime<30||expireingtime>2592000)
		{
			throw new IllegalInputException("Lifetime of an HIT must be between 30 and 31536000 seconds.", "expireingtime");
		}
		this.expireingtime = expireingtime;
	}

	/**
	 * @return the qualifications
	 */
	public QualificationRequirement[] getQualifications() {
		return qualifications;
	}

	/**
	 * @param qualifications
	 *            the qualifications to set
	 */
	public void setQualifications(Iterable<String> qualifications)
			throws IllegalInputException {
		int amount = 0;
		int current = 0;
		QualificationRequirement[] require = null;
		// FROM HERE SET QUALIS
		if(qualifications!= null)
		{
		for (String qualification : qualifications) {
			amount++;
		}
		require = new QualificationRequirement[amount];
		if (amount >= 11) {
			throw new IllegalInputException(
					"Only 10 qualifications can be given, no more","amount");
		}
		if (amount > 0) {
			require = new QualificationRequirement[amount];
			Iterator<String> iter = qualifications.iterator();
			while (iter.hasNext()) {
				String toParse = iter.next();
				require[current] = RequirementByString(toParse);

				current++;
			}

		}
		}
		this.qualifications = require;
	}

	private QualificationRequirement RequirementByString(String qualification) throws IllegalInputException{
		QualificationRequirement require = new QualificationRequirement();

		String quali = qualification.replace(" ", "");
		String[] splittetQuali = quali.split(",");
		if (splittetQuali.length != 4) {
			throw new IllegalInputException(
					"Qualification Requirement is not correct. QualificationType(String) IntegerValue(int) Comparator(String) Required for Preview(boolean) is needed separated by a \",\" each.","qualification");
		}
		require.setQualificationTypeId(splittetQuali[0]);
		try{
		require.setIntegerValue(Integer.parseInt(splittetQuali[1]));
		}catch(NumberFormatException e)
		{
			throw new IllegalInputException("Not well formed. Must be an integer" + splittetQuali[1],"qualification");
		}
		try{
		require.setComparator(Comparator.fromString(splittetQuali[2]));
		}catch(IllegalArgumentException e)
		{
			throw new IllegalInputException("Not well formed. Must be an Comparator type." + e.getMessage(), "qualification");
		}
		if(splittetQuali[3].compareTo("false")==0||splittetQuali[3].compareTo("true")==0)
		{
		require.setRequiredToPreview(Boolean.valueOf(splittetQuali[3]));
		}else
		{
			throw new IllegalInputException("Boolean for required to preview must be true or false.","qualification");
		}
		return require;
	}

	/**
	 * @return the xml representation of an externalQuestionURL with it's
	 *         properties
	 */
	public String getExternalQuestion() {
		return externalQuestion;
	}

	/**
	 * @return the numAssignments
	 */
	public int getNumAssignments() {
		return numAssignments;
	}

	/**
	 * @param numAssignments
	 *            the numAssignments to set
	 */
	public void setNumAssignments(int numAssignments) throws IllegalInputException{
		if(numAssignments<1 || numAssignments >1000000000)
		{
			throw new IllegalInputException("Number of available assignments must be between 1 and 1000000000.","numAssignments");
		}
		this.numAssignments = numAssignments;
	}

	/**
	 * @return the estimateTime
	 */
	public int getEstimateTime() {
		return estimateTime;
	}

	/**
	 * @param estimateTime
	 *            the estimateTime to set
	 */
	public void setEstimateTime(int estimateTime) throws IllegalInputException{
		if(estimateTime<30||estimateTime>31536000)
		{
			throw new IllegalInputException("Completiontime for a worker to complete a hit must be between 30 and 31536000 seconds.","estimateTime");
		}
		this.estimateTime = estimateTime;
	}

}
