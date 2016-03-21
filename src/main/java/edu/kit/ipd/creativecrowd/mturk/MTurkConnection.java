package edu.kit.ipd.creativecrowd.mturk;

import com.amazonaws.mturk.service.exception.ServiceException;
import com.amazonaws.mturk.requester.HIT;
import com.amazonaws.mturk.service.axis.RequesterService;
import com.amazonaws.mturk.util.ClientConfig;

import edu.kit.ipd.creativecrowd.util.GlobalApplicationConfig;

/**
 * @author Tobias This class creates the connection to MTurk and simplifies the
 *         interface to MTurk for the program
 */
public class MTurkConnection {
	private RequesterService service;

	/**
	 * constructor to MTurk who automatically sets the requesterData
	 * 
	 * @throws ConnectionFailedException
	 *         if the SecretAccessKey is not valid or any other Connection
	 *         problems occur
	 */
	public MTurkConnection() throws ConnectionFailedException {
		setAccessData();
		if (!validateRequesterData()) {
			throw new ConnectionFailedException("SecretKey is not valid");
		}
	}

	/**
	 * Checks wether the account has sufficent amount of money
	 * 
	 * @param amountInCents the amount in cents to check
	 * @return true if the account has more money than the given param, false if not
	 * @throws ConnectionFailedException is thrown if there are any problems connecting to MTurk or using the IDS
	 * @throws IllegalInputException is thrown if the amountInCents is less than zero
	 */
	public boolean checkBudget(int amountInCents) throws ConnectionFailedException, IllegalInputException
	{
		boolean isEnough = false;
		if (amountInCents <= 0)
		{
			throw new IllegalInputException("Budget to check must be higher than zero.", "amountInCents");
		}
		try {
			if ((service.getAccountBalance() - amountInCents / 100.0) >= 0)
			{
				isEnough = true;
			}
		} catch (ServiceException u) {
			throw new ConnectionFailedException(u.getMessage());
		}
		return isEnough;
	}

	/**
	 * Publishs a HIT on AmazonMTurk
	 * 
	 * @param spec
	 *        the Specs of the HIT which gets published
	 * @return The HITId of the published HIT
	 * @throws ConnectionFailedException
	 *         is thrown if there are any problems connecting to MTurk or
	 *         publishing the HIT
	 */
	public String publishHIT(HITSpec spec) throws ConnectionFailedException {
		HIT hit = null;
		try {
			/*-?|Anika|Anika|c8|?*/
			hit = service.createHIT(null, spec.getTitle(), spec.getDescription(), spec.getKeywords(), spec.getExternalQuestion(), spec.getRewardCents() / 100.0, (long) spec.getCompletiontime(),
					(long) spec.getExpireingtime(), (long) spec.getExpireingtime(), spec.getNumAssignments(), null, spec.getQualifications(), null, null, null, null);
		} catch (ServiceException u) {
			throw new ConnectionFailedException(u.getMessage());
		} catch (Exception e) {
			throw new ConnectionFailedException(e.getMessage());
		}
		return hit.getHITId();
	}

	/**
	 * sets The accessData of The requesterAccount and the service URL
	 */
	private void setAccessData() {
		ClientConfig config = new ClientConfig();
		config.setAccessKeyId(GlobalApplicationConfig.getAWSAccessKeyID());
		config.setSecretAccessKey(GlobalApplicationConfig.getAWSSecretKey());
		if (GlobalApplicationConfig.isSandbox()) {
			config.setServiceURL("https://mechanicalturk.sandbox.amazonaws.com/?Service=AWSMechanicalTurkRequester");
		} else {
			config.setServiceURL("https://mechanicalturk.amazonaws.com/?Service=AWSMechanicalTurkRequester");
		}
		service = new RequesterService(config);
	}

	/**
	 * Disables the HIT on MTurk
	 * 
	 * @param hitId
	 *        The Id of the Hit which should be disabled
	 */
	public void endHIT(String hitId) throws IllegalInputException {
		try {
			service.disableHIT(hitId);
		} catch (ServiceException e) {
			throw new IllegalInputException("Failed to disable HIT with id"
					+ hitId + "  " + "error Message" + e.getMessage(), "hitId");
		}
	}

	/**
	 * validates the Requester secretAccess key
	 * 
	 * @return true if the secretAccessKey ist valid, false if not
	 */
	public boolean validateRequesterData() throws ConnectionFailedException {
		boolean validate = false;// TODO cause kann leer sein
		try {
			service.getAccountBalance();
			validate = true;
		} catch (ServiceException e) {
			validate = false;
			try {
				String cause = e.getCause().toString();
				if (cause.contains("java.net.UnknownHostException")) {
					throw new ConnectionFailedException(
							"Can't connect to the Amazon MTurk Server.");
				}
			} catch (NullPointerException y) {
				validate = false;
			}
		} catch (NullPointerException z) {
			validate = false;
		}
		return validate;
	}

	/**
	 * Approves a HIT on MTurk
	 * 
	 * @param AssignmentId
	 *        the Id which should get approved
	 * @param feedback
	 *        the feedback which is given to the worker
	 */
	public void approveHIT(AssignmentId AssignmentId, String feedback)
			throws IllegalInputException {
		try {
			service.approveAssignment(AssignmentId.getId(), feedback);
		} catch (ServiceException e) {
			/**
			 * throw new IllegalInputException(
			 * "Unable to approve HIT with assignmentId:  " + AssignmentId
			 * + "  " + e.getMessage(), "AssignmentId");
			 **/
		}
	}

	/**
	 * Sends a Bonus to a worker
	 * 
	 * @param asId
	 *        the AssignmentID the worker gets the Bonus for
	 * @param workerId
	 *        the ID of the worker who gets the Bonus
	 * @param bonusCent
	 *        the Bonus in Cent
	 * @param message
	 *        The message the worker receives
	 */
	public void sendBonus(AssignmentId asId, WorkerId workerId, int bonusCent,
			String message) throws IllegalInputException {
		if (bonusCent <= 0 | bonusCent > 31536000) {
			throw new IllegalInputException(
					"Bonus Cent is no valid entry. Value must be between 0 and 3153600",
					"bonusCent");
		}
		try {
			service.grantBonus(workerId.getId(), bonusCent / 100.0,
					asId.getId(), message);
		} catch (ServiceException e) {
			/**
			 * throw new IllegalInputException(
			 * "Unable to send Bonus to AssignmentId " + asId
			 * + "  worker ID " + workerId + "   " + "message  "
			 * + e.getMessage(), "asIdworkerId");
			 **/
		}
	}

	/**
	 * rejects a Assignment that the worker doesn't get basic money
	 * 
	 * @param asId
	 *        the ID of the rejected Assignment
	 * @param reason
	 *        The feedback reason the worker gets
	 */
	public void rejectAssignment(AssignmentId asId, String reason)
			throws IllegalInputException {
		if (reason == null) {
			throw new IllegalInputException(
					"reason to reject a assignment must not be null", "reason");
		}
		try {
			service.rejectAssignment(asId.getId(), reason);
		} catch (ServiceException e) {
			throw new IllegalInputException(
					"Unable to reject assignment with id" + asId + "   "
							+ e.getMessage(), "asId");
		}
	}

	/**
	 * Returns the amount of available assignments for a hitID
	 * 
	 * @param hitId
	 *        The ID from where the number of actual available assignments
	 *        should be returned
	 * @return the amount of available hits for the hit id
	 * @throws IllegalInputException
	 */
	public int getAvailableAssignments(String hitId)
			throws IllegalInputException {
		HIT hit = null;
		try {
			hit = service.getHIT(hitId);
		} catch (ServiceException e) {
			throw new IllegalInputException("Can't get HIT with ID " + hitId
					+ " error message " + e.getMessage(), hitId);
		}
		return hit.getNumberOfAssignmentsAvailable();
	}

	/**
	 * Extends the number of available assignments to the number of
	 * nrOfAssignments
	 * 
	 * @param hitId
	 *        the Id from which the amount of available assignments gets
	 *        signed to
	 * @param nrOfAssignments
	 *        the number of assignments which gets add to the excisting ones
	 */
	public void extendAssignmentNumber(int nrOfAssignments, String hitId)
			throws IllegalInputException {
		HIT hit = null;
		try {
			hit = service.getHIT(hitId);
		} catch (ServiceException e) {
			throw new IllegalInputException("Can't get HIT with ID " + hitId
					+ " error message " + e.getMessage(), hitId);
		}
		int oldHitAmount = hit.getMaxAssignments();
		int newHitAmount = nrOfAssignments - oldHitAmount;
		if (newHitAmount < 0) {
			throw new IllegalInputException(
					"Number of assignments to extend to must be higher than zero",
					"nrOfAssignments");
		}
		if (newHitAmount == 0)
		{
			return;
		}
		try {
			service.extendHIT(hitId, newHitAmount, 60l);
		} catch (ServiceException e) {
			throw new IllegalInputException("Can't get HIT with ID " + hitId
					+ " error message " + e.getMessage(), hitId);
		}

	}

}
