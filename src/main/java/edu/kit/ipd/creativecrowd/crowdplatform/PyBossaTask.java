package edu.kit.ipd.creativecrowd.crowdplatform;

/**
 * This class is the object representation of a task on pybossa
 * it is used primarily from JACKSON.
 * @author Robin
 *
 */
public class PyBossaTask {
	
	// these fields are all part of a pybossa task, we cannot add properties to those.
	//if you want something else stored. put it in the subclass.
	private PyBossaInfo info;
	private int project_id;		//SAME
	private int n_answers; //NEVER, NEVER RENAME THIS
	
	
	//private int estimateTime; //Dropped
	//private int workerTimeout; //Dropped
	//private int completiontime; // dropped
	//private int expireingtime; //dropped
	//private QualificationRequirement[] qualifications; //dropped
	protected PyBossaTask() {
		
	}
	/**
	 * This subclass contains the info field from the pybossa task. info may contain everything 
	 * we might need at the pybossa server.
	 * @author Robynovitsch
	 *
	 */
	static class PyBossaInfo {
		private String title;
		private String description;
		private String keywords;
		private String question;
		private int rewardCents;
		// For some obscure reason the pybossa script really wants to load an image this is a temporary solution
		private String url_b = "https://pbs.twimg.com/profile_images/2466029543/mdaqaefyp99zmbx7l8qr.jpeg";
		protected PyBossaInfo() {
			
		}
		protected PyBossaInfo(String t, String d, String q, String k, int rewardCent) {
			this.title = t;
			this.setDescription(d);
			this.setKeywords(k);
			this.setQuestion(q);
			this.setRewardCents(rewardCent);
		}

		public String getTitle() {
			return this.title;
		}

		public String getDescription() {
			return description;
		}

		private void setDescription(String description) {
			this.description = description;
		}

		public String getKeywords() {
			return keywords;
		}

		private void setKeywords(String keywords) {
			this.keywords = keywords;
		}

		public String getQuestion() {
			return question;
		}

		private void setQuestion(String question) {
			this.question = question;
		}
		public String getUrl_b() {
			return this.url_b;
		}

		public int getRewardCents() {
			return rewardCents;
		}

		public void setRewardCents(int rewardCents) {
			this.rewardCents = rewardCents;
		}
		
	}
	/**
	 * this creates a pybossa task from a hitspec originally intended for mturk.
	 * it needs a project id, so we use the one the server has given us.
	 * @param spec the hitspec
	 * @param project_id the project id
	 */
	protected PyBossaTask(HITSpec spec, int project_id) {
		String s = spec.getExternalURL();
		if(s.indexOf("MTurk") != -1) {
			s = s.substring(0, s.indexOf("MTurk"));
		}
		s += "PyBossa";
		this.info = new PyBossaInfo(spec.getTitle(), spec.getDescription(), s, spec.getKeywords(), spec.getRewardCents());
		this.n_answers = spec.getNumAssignments();
		this.project_id = project_id;
	}
	/**
	 * This method returns the amount of answers neeeded.
	 * NEVER CHANGE THIS METHODS SIGNATURE
	 * original signature: getN_answers()
	 * @return the amount of answers
	 */
	public int getN_answers() {
		return this.n_answers;
	}
	/**
	 * this method sets the amount of answers this taskobject 
	 * accepts.
	 * @param n_answers the amount of answers
	 */
	public void setN_answers(int n_answers) {
		this.n_answers = n_answers;
	}






	public PyBossaInfo getInfo() {
		return info;
	}
	/**
	 * THis method gives back the project ID
	 * 
	 * NEVER CHANGE THIS METHOD SIGNATURE AS IT IS CONVERTED TO JSON AND READ BY PYBOSSA 
	 * 
	 * 
	 * (original method signature: getProject_id)
	 * @return the project id
	 */
	public int getProject_id() {
		return project_id;
	}








}
