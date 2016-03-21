package edu.kit.ipd.chimpalot.jsonclasses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.kit.ipd.creativecrowd.readablemodel.Answer;
import edu.kit.ipd.creativecrowd.readablemodel.CalibrationAnswer;
import edu.kit.ipd.creativecrowd.readablemodel.CalibrationQuestion;
import edu.kit.ipd.creativecrowd.readablemodel.ConfigModel;
import edu.kit.ipd.creativecrowd.readablemodel.ControlQuestion;
import edu.kit.ipd.creativecrowd.readablemodel.ExperimentType;
import edu.kit.ipd.creativecrowd.readablemodel.Rating;
import edu.kit.ipd.creativecrowd.crowdplatform.PlatformIdentity;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCalibrationQuestion;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.TypeOfTask;
import edu.kit.ipd.creativecrowd.readablemodel.Worker;

/**
 * A "POJO" representation of configfiles for experiments. This class is used mainly to read and
 * create Json-Files with Jackson.
 * 
 * @author Thomas Friedel
 *
 */
public class ConfigModelJson implements ConfigModel {
	
	private String id;
	
	private String taskQuestion;
	private String ratingTaskQuestion;
	private String taskDescription;
	private String taskTitle;
	private String[] taskTags;
	private String pictureURL;
	private String taskSourceURL;
	private String[] calibQuestions;
	private ControlQuestionJson[] controlQuestions;
	private int maxCreativeTask;
	private int maxRatingTask;
	private int budget;
	private boolean sendCreativeToMTurk;
	private boolean sendCreativeToPyBossa;
	private boolean sendRatingToMTurk;
	private boolean sendRatingToPyBossa;
	private int basicPaymentMTurk;
	private int basicPaymentPyBossa;
	private int paymentPerTaskCrMTurk;
	private int paymentPerTaskRaMTurk;
	private int paymentPerTaskCrPyBossa;
	private int paymentPerTaskRaPyBossa;
	private float averageRatingThreshold;
	private int totalTaskCountThreshold;
	private String evaluationType;
	private Map<String, String> strategy;
	private Iterable<String> qualificationsPyBossa;
	private Iterable<String> qualificationsMTurk;
	private List<RatingOptionJson> ratingOptions = new ArrayList<RatingOptionJson>();
	private ExperimentType experimentType = ExperimentType.Undefined;
	private List<String> blockedWorkers = new ArrayList<String>();
	
	/**
	 * Creates a POJO representation of an existing ConfigModel
	 * 
	 * @param configModel the configModel to create this ConfigModel from
	 * @throws DatabaseException if it fails to read the given configModel
	 */
	public ConfigModelJson(ConfigModel configModel) throws DatabaseException {
		this.setTaskQuestion(configModel.getTaskQuestion());
		this.setRatingTaskQuestion(configModel.getRatingTaskQuestion());
		this.setTaskDescription(configModel.getTaskDescription());
		this.setTaskTitle(configModel.getTaskTitle());
		this.setTaskTags(configModel.getTaskTags());
		this.setPictureURL(configModel.getPictureURL());
		this.setTaskSourceURL(configModel.getTaskSourceURL());
		this.setCalibQuestions(configModel.getCalibQuestions());
		this.setControlQuestions(configModel.getControlQuestions());
		this.setMaxCreativeTask(configModel.getMaxCreativeTask());
		this.setMaxRatingTask(configModel.getMaxRatingTask());
		this.setBudget(configModel.getBudget());
		this.setSendCreativeToMTurk(configModel.getSendCreativeTo(PlatformIdentity.MTurk));
		this.setSendCreativeToPyBossa(configModel.getSendCreativeTo(PlatformIdentity.PyBossa));
		this.setSendRatingToMTurk(configModel.getSendRatingTo(PlatformIdentity.MTurk));
		this.setSendRatingToPyBossa(configModel.getSendRatingTo(PlatformIdentity.PyBossa));
		this.setBasicPaymentMTurk(configModel.getBasicPayment(PlatformIdentity.MTurk));
		this.setBasicPaymentPyBossa(configModel.getBasicPayment(PlatformIdentity.PyBossa));
		this.setPaymentPerTaskCrMTurk(configModel.getPaymentPerTask(PlatformIdentity.MTurk, TypeOfTask.Creative));
		this.setPaymentPerTaskRaMTurk(configModel.getPaymentPerTask(PlatformIdentity.MTurk, TypeOfTask.Rating));
		this.setPaymentPerTaskCrPyBossa(configModel.getPaymentPerTask(PlatformIdentity.PyBossa, TypeOfTask.Creative));
		this.setPaymentPerTaskRaPyBossa(configModel.getPaymentPerTask(PlatformIdentity.PyBossa, TypeOfTask.Rating));
		this.setEvaluationType(configModel.getEvaluationType());
		this.setStrategy(configModel.getStrategy());
		this.setQualificationsMTurk(configModel.getQualifications(PlatformIdentity.MTurk));
		this.setQualificationsPyBossa(configModel.getQualifications(PlatformIdentity.PyBossa));
		this.setRatingOptions(configModel.getRatingOptions());
		this.setAverageRatingThreshold(configModel.getAverageRatingThreshold());
		this.setTotalTaskCountThreshold(configModel.getTotalTaskCountThreshold());
		this.setExperimentType(configModel.getExperimentType());
		this.setBlockedWorkers(configModel.getBlockedWorkers());
	}
	
	
	public void setExperimentType(ExperimentType type) {
		this.experimentType = (type == null)? ExperimentType.Undefined : type;
	}


	/**
	 * An empty constructor with all parameters staying {@code null} or {@code 0}.
	 */
	public ConfigModelJson() {
		
	}

	/**
	 * @param id the id to set
	 */
	public void setID(String id) {
		this.id = id;	
	}
	
	/**
	 * @param taskQuestion the taskQuestion to set
	 */
	public void setTaskQuestion(String taskQuestion) {
		this.taskQuestion = (taskQuestion == null)? "" : taskQuestion;
	}
	
	/**
	 * @param ratingTaskQuestion the ratingTaskQuestion to set
	 */
	public void setRatingTaskQuestion(String ratingTaskQuestion) {
		this.ratingTaskQuestion = (ratingTaskQuestion == null)? "" : ratingTaskQuestion;
	}

	/**
	 * @param taskDescription the taskDescription to set
	 */
	public void setTaskDescription(String taskDescription) {
		this.taskDescription = (taskDescription == null)? "" : taskDescription;
	}

	/**
	 * @param taskTitle the taskTitle to set
	 */
	public void setTaskTitle(String taskTitle) {
		this.taskTitle = (taskTitle == null)? "" : taskTitle;
	}

	/**
	 * @param taskTags the taskTags to set
	 */
	public void setTaskTags(String[] taskTags) {
		this.taskTags = (taskTags == null)? new String[0] : taskTags;
	}

	/**
	 * @param pictureURL the pictureURL to set
	 */
	public void setPictureURL(String pictureURL) {
		this.pictureURL = (pictureURL == null)? "" : pictureURL;
	}

	/**
	 * @param taskSourceURL the taskSourceURL to set
	 */
	public void setTaskSourceURL(String taskSourceURL) {
		this.taskSourceURL = (taskSourceURL == null)? "" : taskSourceURL;
	}
	
	/**
	 * @param calibQuestions the calibQuestions to set
	 */
	@JsonIgnore
	public void setCalibQuestions(CalibrationQuestion[] calibQuestions) {
		this.calibQuestions = (calibQuestions == null)?
				new String[0] : new String[calibQuestions.length];
		for (int i = 0; i < calibQuestions.length; i++) {
			this.calibQuestions[i] = calibQuestions[i].getID();
		}
	}
	
	@JsonProperty("calibQuestions")
	public void setCalibQuestionsJson(String[] calibQuestions) {
		this.calibQuestions = (calibQuestions == null)? new String[0] : calibQuestions;
	}

	/**
	 * @param controlQuestions the controlQuestions to set
	 * @throws DatabaseException if reading from database fails
	 */
	@JsonIgnore
	public void setControlQuestions(ControlQuestion[] controlQuestions) throws DatabaseException {
		this.controlQuestions = (controlQuestions == null)?
				new ControlQuestionJson[0] : new ControlQuestionJson[controlQuestions.length];
		for (int i = 0; i < controlQuestions.length; i++) {
			this.controlQuestions[i] = new ControlQuestionJson(controlQuestions[i]);
		}
	}
	
	@JsonProperty("controlQuestions")
	public void setControlQuestionsJson(ControlQuestionJson[] controlQuestions) throws DatabaseException {
		setControlQuestions(controlQuestions);
	}

	/**
	 * @param maxCreativeTask the maxCreativeTask to set
	 */
	public void setMaxCreativeTask(int maxCreativeTask) {
		this.maxCreativeTask = maxCreativeTask;
	}

	/**
	 * @param maxRatingTask the maxRatingTask to set
	 */
	public void setMaxRatingTask(int maxRatingTask) {
		this.maxRatingTask = maxRatingTask;
	}

	/**
	 * @param budget the budget to set
	 */
	public void setBudget(int budget) {
		this.budget = budget;
	}

	/**
	 * @param sendCreativeToMTurk the sendCreativeToMTurk to set
	 */
	public void setSendCreativeToMTurk(boolean sendCreativeToMTurk) {
		this.sendCreativeToMTurk = sendCreativeToMTurk;
	}

	/**
	 * @param sendCreativeToPyBossa the sendCreativeToPyBossa to set
	 */
	public void setSendCreativeToPyBossa(boolean sendCreativeToPyBossa) {
		this.sendCreativeToPyBossa = sendCreativeToPyBossa;
	}

	/**
	 * @param sendRatingToMTurk the sendRatingToMTurk to set
	 */
	public void setSendRatingToMTurk(boolean sendRatingToMTurk) {
		this.sendRatingToMTurk = sendRatingToMTurk;
	}

	/**
	 * @param sendRatingToPyBossa the sendRatingToPyBossa to set
	 */
	public void setSendRatingToPyBossa(boolean sendRatingToPyBossa) {
		this.sendRatingToPyBossa = sendRatingToPyBossa;
	}

	/**
	 * @param basicPaymentMTurk the basicPaymentMTurk to set
	 */
	public void setBasicPaymentMTurk(int basicPaymentMTurk) {
		this.basicPaymentMTurk = basicPaymentMTurk;
	}

	/**
	 * @param basicPaymentPyBossa the basicPaymentPyBossa to set
	 */
	public void setBasicPaymentPyBossa(int basicPaymentPyBossa) {
		this.basicPaymentPyBossa = basicPaymentPyBossa;
	}

	/**
	 * @param paymentPerTaskCrMTurk the paymentPerTaskCrMTurk to set
	 */
	public void setPaymentPerTaskCrMTurk(int paymentPerTaskCrMTurk) {
		this.paymentPerTaskCrMTurk = paymentPerTaskCrMTurk;
	}

	/**
	 * @param paymentPerTaskRaMTurk the paymentPerTaskRaMTurk to set
	 */
	public void setPaymentPerTaskRaMTurk(int paymentPerTaskRaMTurk) {
		this.paymentPerTaskRaMTurk = paymentPerTaskRaMTurk;
	}

	/**
	 * @param paymentPerTaskCrPyBossa the paymentPerTaskCrPyBossa to set
	 */
	public void setPaymentPerTaskCrPyBossa(int paymentPerTaskCrPyBossa) {
		this.paymentPerTaskCrPyBossa = paymentPerTaskCrPyBossa;
	}

	/**
	 * @param paymentPerTaskRaPyBossa the paymentPerTaskRaPyBossa to set
	 */
	public void setPaymentPerTaskRaPyBossa(int paymentPerTaskRaPyBossa) {
		this.paymentPerTaskRaPyBossa = paymentPerTaskRaPyBossa;
	}

	/**
	 * @param evaluationType the evaluationType to set
	 */
	public void setEvaluationType(String evaluationType) {
		this.evaluationType = (evaluationType == null)? "" : evaluationType;
	}

	/**
	 * @param strategy the strategy to set
	 */
	public void setStrategy(Map<String, String> strategy) {
		this.strategy = (strategy == null)? new HashMap<String, String>() : strategy;
	}

	/**
	 * @param iterable the ratingOptions to set
	 */
	@JsonIgnore
	public void setRatingOptions(Map<String, Float> iterable) throws DatabaseException {
		this.ratingOptions =  new ArrayList<RatingOptionJson>();
		if (iterable != null) {
			for (Entry<String, Float> entry : iterable.entrySet()) {
				RatingOptionJson ro = new RatingOptionJson();
				ro.setText(entry.getKey());
				ro.setValue(entry.getValue());
				this.ratingOptions.add(ro);
			}
		}
	}
	
	@JsonProperty("ratingOptions")
	public void setRatingOptionsJson(Iterable<RatingOptionJson> iterable) throws DatabaseException {
		this.ratingOptions = new ArrayList<RatingOptionJson>();
		if (iterable != null) {
			for (RatingOptionJson ro : iterable) {
				this.ratingOptions.add(ro);
			}
		}
	}

	/**
	 * @param averageRatingThreshold the averageRatingThreshold to set
	 */
	public void setAverageRatingThreshold(float averageRatingThreshold) {
		this.averageRatingThreshold = averageRatingThreshold;
	}

	/**
	 * @param totalTaskCountThreshold the totalTaskCountThreshold to set
	 */
	public void setTotalTaskCountThreshold(int totalTaskCountThreshold) {
		this.totalTaskCountThreshold = totalTaskCountThreshold;
	}
	
	/**
	 * 
	 * @param iterable a list of the workers to be blocked
	 */
	@JsonIgnore
	public void setBlockedWorkers(Iterable<Worker> iterable) throws DatabaseException {
		this.blockedWorkers = new ArrayList<String>();
		if (iterable != null) {
			for (Worker wrk : iterable) {
				this.blockedWorkers.add(wrk.getID());
			}
		}
	}
	
	@JsonProperty("blockedWorkers")
	public void setBlockedWorkersJson(List<String> iterable) {
		this.blockedWorkers = (iterable == null)? new ArrayList<String>() : iterable;
	}


	/**
	 * @return the basicPaymentMTurk
	 */
	public int getBasicPaymentMTurk() {
		return basicPaymentMTurk;
	}

	/**
	 * @return the basicPaymentPyBossa
	 */
	public int getBasicPaymentPyBossa() {
		return basicPaymentPyBossa;
	}

	/**
	 * @return the qualificationsPyBossa
	 */
	public Iterable<String> getQualificationsPyBossa() {
		return qualificationsPyBossa;
	}

	/**
	 * @return the qualificationsMTurk
	 */
	public Iterable<String> getQualificationsMTurk() {
		return qualificationsMTurk;
	}

	/**
	 * @param qualificationsPyBossa the qualificationsPyBossa to set
	 */
	public void setQualificationsPyBossa(Iterable<String> qualificationsPyBossa) {
		this.qualificationsPyBossa = (qualificationsPyBossa == null)? new ArrayList<String>() : qualificationsPyBossa;
	}

	/**
	 * @param qualificationsMTurk the qualificationsMTurk to set
	 */
	public void setQualificationsMTurk(Iterable<String> qualificationsMTurk) {
		this.qualificationsMTurk = (qualificationsMTurk == null)? new ArrayList<String>() : qualificationsMTurk;
	}

	@Override
	public String getTaskQuestion() {
		return taskQuestion;
	}

	@Override
	public String getTaskDescription() {
		return this.taskDescription;
	}

	@Override
	public String getTaskTitle() {
		return this.taskTitle;
	}

	@Override
	public String[] getTaskTags() {
		return this.taskTags;
	}

	@Override
	public String getPictureURL() {
		return this.pictureURL;
	}

	@Override
	public String getTaskSourceURL() {
		return this.taskSourceURL;
	}

	@Override
	@JsonIgnore
	public CalibrationQuestion[] getCalibQuestions() {
		if (this.calibQuestions != null) {
			CalibrationQuestion[] result = new CalibrationQuestion[this.calibQuestions.length];
			for (int i = 0; i < this.calibQuestions.length; i++) {
				CalibrationQuestionJson temp = new CalibrationQuestionJson();
				temp.setId(this.calibQuestions[i]);
				result[i] = temp;
			}
			return result;
		} else {
			return new CalibrationQuestion[0];
		}
	}
	
	@JsonProperty("calibQuestions")
	public String[] getCalibQuestionsJson() {
		return this.calibQuestions;
	}

	@Override
	@JsonIgnore
	public ControlQuestion[] getControlQuestions() {
		return this.controlQuestions;
	}
	
	@JsonProperty("controlQuestions")
	public ControlQuestionJson[] getControlQuestionJson() {
		return this.controlQuestions;
	}

	@Override
	public int getMaxCreativeTask() {
		return this.maxCreativeTask;
	}

	@Override
	public int getMaxRatingTask() {
		return this.maxRatingTask;
	}

	@Override
	@JsonIgnore //POJO can't handle arguments
	public boolean getSendCreativeTo(PlatformIdentity platform) {
		switch(platform) {
		case MTurk: return this.sendCreativeToMTurk;
		case PyBossa: return this.sendCreativeToPyBossa;
		default: throw new IllegalArgumentException("Non specified platform.");
		}
	}

	@Override
	@JsonIgnore //POJO can't handle arguments
	public boolean getSendRatingTo(PlatformIdentity platform) {
		switch(platform) {
		case MTurk: return this.sendRatingToMTurk;
		case PyBossa: return this.sendRatingToPyBossa;
		default: throw new IllegalArgumentException("Non specified platform.");
		}
	}

	@Override
	public String getEvaluationType() {
		return this.evaluationType;
	}

	@Override
	public Map<String, String> getStrategy() {
		return this.strategy;
	}

	@Override
	public String getID() throws DatabaseException {
		return this.id;
	}

	@Override
	public int getBudget() throws DatabaseException {
		return this.budget;
	}

	@Override
	@JsonIgnore //POJO can't handle arguments
	public int getBasicPayment(PlatformIdentity platform) throws DatabaseException {
		switch(platform) {
		case MTurk: return this.basicPaymentMTurk;
		case PyBossa: return this.basicPaymentPyBossa;
		default: throw new IllegalArgumentException("Non specified platform.");
		}
	}

	@Override
	@JsonIgnore //POJO can't handle arguments
	public int getPaymentPerTask(PlatformIdentity platform, TypeOfTask type) throws DatabaseException {
		switch(platform) {
		case MTurk: return (type == TypeOfTask.Creative)? this.paymentPerTaskCrMTurk : this.paymentPerTaskRaMTurk;
		case PyBossa: return (type == TypeOfTask.Creative)? this.paymentPerTaskCrPyBossa : this.paymentPerTaskRaPyBossa;
		default: throw new IllegalArgumentException("Non specified platform.");
		}
	}

	public boolean isSendCreativeToMTurk() {
		return sendCreativeToMTurk;
	}

	public boolean isSendCreativeToPyBossa() {
		return sendCreativeToPyBossa;
	}

	public boolean isSendRatingToMTurk() {
		return sendRatingToMTurk;
	}

	public boolean isSendRatingToPyBossa() {
		return sendRatingToPyBossa;
	}

	public int getPaymentPerTaskCrMTurk() {
		return paymentPerTaskCrMTurk;
	}

	public int getPaymentPerTaskRaMTurk() {
		return paymentPerTaskRaMTurk;
	}

	public int getPaymentPerTaskCrPyBossa() {
		return paymentPerTaskCrPyBossa;
	}

	public int getPaymentPerTaskRaPyBossa() {
		return paymentPerTaskRaPyBossa;
	}

	@Override
	@JsonIgnore //Not needed in frontend
	public String getExperimentID() throws DatabaseException {
		//Always null here.
		return null;
	}

	@Override
	public String getRatingTaskQuestion() throws DatabaseException {
		return this.ratingTaskQuestion;
	}

	@Override
	@JsonIgnore //POJO can't handle arguments
	public Iterable<String> getQualifications(PlatformIdentity platform) throws DatabaseException {
		switch (platform) {
		case MTurk: return this.qualificationsMTurk;
		case PyBossa: return this.qualificationsPyBossa;
		default: throw new IllegalArgumentException("Non specified platform.");
		}
	}

	@Override
	@JsonIgnore
	public Map<String, Float> getRatingOptions() throws DatabaseException {
		Map<String, Float> result = new HashMap<String, Float>();
		if (this.ratingOptions != null) {
			for (RatingOptionJson ro : this.ratingOptions) {
				result.put(ro.getText(), ro.getValue());
			}
		}
		return result;
	}
	
	@JsonProperty("ratingOptions")
	public Iterable<RatingOptionJson> getRatingOptionsJson() {
		return this.ratingOptions;
	}

	@Override
	public float getAverageRatingThreshold() throws DatabaseException {
		return this.averageRatingThreshold;
	}

	@Override
	public int getTotalTaskCountThreshold() throws DatabaseException {
		return this.totalTaskCountThreshold;
	}

	@Override
	public ExperimentType getExperimentType() throws DatabaseException {
		return this.experimentType;
	}

	@Override
	@JsonIgnore
	public Iterable<Worker> getBlockedWorkers() throws DatabaseException {
		List<Worker> result = new ArrayList<Worker>();
		if (this.blockedWorkers != null) {
			for (String id : this.blockedWorkers)
			result.add(new WorkerJson(id));
		}
		return result;
	}
	
	@JsonProperty("blockedWorkers")
	public Iterable<String> getBlockedWorkersJson() throws DatabaseException {
		return this.blockedWorkers;
	}
	
	
	/**
	 * Just for an id...
	 */
	private class WorkerJson implements Worker { //TODO maybe change blockedWorkers in ConfigModel to Strings
		
		private String id;
		
		private WorkerJson(String id) {
			this.id = id;
		}
		
		@Override
		public String getName() throws DatabaseException {
			return null;
		}

		@Override
		public String getEmail() throws DatabaseException {
			return null;
		}

		@Override
		public String getID() throws DatabaseException {
			return id;
		}

		@Override
		public int getCredit() throws DatabaseException {
			return 0;
		}

		@Override
		public Iterable<? extends CalibrationAnswer> getCalibrationAnswers() throws DatabaseException {
			return null;
		}

		@Override
		public Iterable<? extends Answer> getAnswers() throws DatabaseException {
			return null;
		}

		@Override
		public Iterable<? extends Rating> getRatings() throws DatabaseException {
			return null;
		}

		@Override
		public Iterable<? extends MutableCalibrationQuestion> getDoneCalibQuestWorker() throws DatabaseException {
			return null;
		}

		@Override
		public boolean isBlocked() throws DatabaseException {
			return false;
		}

		@Override
		public PlatformIdentity getPlatform() throws DatabaseException {
			return null;
		}
		
	}
}
