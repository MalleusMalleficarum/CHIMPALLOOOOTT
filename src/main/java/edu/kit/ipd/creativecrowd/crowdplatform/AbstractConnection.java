package edu.kit.ipd.creativecrowd.crowdplatform;

import edu.kit.ipd.chimpalot.util.Logger;



public abstract class AbstractConnection {
    
    public boolean checkBudget(int amountInCents)  throws ConnectionFailedException, IllegalInputException {
        Logger.log("Accessed unimplemented method: checkBudget in AbstractConnection");
        return true;
    }
    public String publishHIT(HITSpec spec) throws ConnectionFailedException {
        Logger.log("Accessed unimplemented method: publishHIT in AbstractConnection");
        return null;
    }
    public void endHIT(String hitId) throws IllegalInputException {
        Logger.log("Accessed unimplemented method: endHIT in AbstractConnection");
    }
    public boolean validateRequesterData() throws ConnectionFailedException {
        Logger.log("Accessed unimplemented method: validateRequesterData in AbstractConnection");
        return true;
    }
    public void approveHIT(AssignmentId AssignmentId, WorkerId workerId, String feedback) throws IllegalInputException {
        Logger.log("Accessed unimplemented method: approveHIT in AbstractConnection");
    }
    public void sendBonus(AssignmentId asId, WorkerId workerId, int bonusCent, String message) throws IllegalInputException {
        Logger.log("Accessed unimplemented method: sendBonus in AbstractConnection");
    }
    public void rejectAssignment(AssignmentId asId, String reason) throws IllegalInputException {
        Logger.log("Accessed unimplemented method: rejectAssignment in AbstractConnection");
    }
    public int getAvailableAssignments(String hitId) throws IllegalInputException {
        Logger.log("Accessed unimplemented method: getAvailableAssignments in AbstractConnection");
        return 0;
    }
    public void extendAssignmentNumber(int nrOfAssignments, String hitId) throws IllegalInputException {
        Logger.log("Accessed unimplemented method: extendAssignmentNumber in AbstractConnection");
    }
    public PlatformIdentity getPlatformIdentity() {
        Logger.log("Accessed unimplemented method: getPlatformIdentity in AbstractConnection");
        return PlatformIdentity.Unspecified;
    }
    public String getAbbrevation() {
    	return null;
    }
}
