package edu.kit.ipd.creativecrowd.transformer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.kit.ipd.chimpalot.util.Logger;
import edu.kit.ipd.creativecrowd.crowdplatform.AbstractConnection;
import edu.kit.ipd.creativecrowd.crowdplatform.AssignmentId;
import edu.kit.ipd.creativecrowd.crowdplatform.ConnectionFailedException;
import edu.kit.ipd.creativecrowd.crowdplatform.HITSpec;
import edu.kit.ipd.creativecrowd.crowdplatform.IllegalInputException;
import edu.kit.ipd.creativecrowd.crowdplatform.MTurkConnection;
import edu.kit.ipd.creativecrowd.crowdplatform.PlatformIdentity;
import edu.kit.ipd.creativecrowd.crowdplatform.PyBossaConnection;
import edu.kit.ipd.creativecrowd.crowdplatform.WorkerId;
/**
 * This Class represents a transformer which distributes the requests to MTurk 
 * to other Crowdworking platforms. 
 * @author Robin
 *
 */
public class Transformer {

    private List<AbstractConnection> connections;
    private InternalLogicMediator mediator;
    private String platformsAddedLater ="";
    private HITSpec mySpec;
    private String idAfterPublish;
    
    /**
     * The Transformer is created from the ConfigFileInterpreter and does not 
     * need to be created from elsewhere.
     * @param mediator
     */
    protected Transformer(InternalLogicMediator mediator) {
    	this.connections = new ArrayList<>();
    	this.mediator = mediator;
    }
    protected void addConnection(AbstractConnection c) {
    	connections.add(c);
    }
    /**
     * Publishes the HIT on another connection, only works if there has actually been an experiment before
     * @param c the COnnection
     * @throws ConnectionFailedException if we cannot connect to it.
     */
    public void additionalPublish(AbstractConnection c) throws ConnectionFailedException {
    	if(mySpec == null) {
    		Logger.log("It appears that no hit has been published before using this Transformer, are you certain"
    				+ "that you know what you're doing?");
    		throw new ConnectionFailedException();
    	}
    	connections.add(c);
    	String id = c.publishHIT(mySpec);
    	platformsAddedLater += "?" + c.getPlatformIdentity().toString() + "|" + id;
    	
    }
    public List<AbstractConnection> getConnections() {
    	return this.connections;
    }
    public boolean containsConnection(PlatformIdentity id) {
    	for(AbstractConnection c : connections) {
    		if(c.getPlatformIdentity() == id) {
    			return true;
    		}
    	}
    	return false;
    }
    /**
     * removes a connection, might cause a double end to an id if the endHIT method 
     * is called later, double ends are not problematic.
     * @param c the Connection
     * @throws IllegalInputException if something cannot be deleted.
     */
    public void removeConnection(PlatformIdentity id) throws IllegalInputException {
    	if(id == null) {
    		throw new IllegalInputException("", "");
    	}
    	for(AbstractConnection c : connections) {
    		if(c.getPlatformIdentity() == id) {
    			String idOfPlatform = mediator.getID(idAfterPublish, id);
        		c.endHIT(idOfPlatform);
        		connections.remove(c);
    		}
    	}
    	
    	
    	
    }
    /**
     * This method will update changes to the transformer
     * @param id the platform identits
     * @param oldV whether the old config included this platform
     * @param newV whether the new config includes this platform 
     * @throws IllegalInputException if the remove fails
     * @throws ConnectionFailedException if the publish fails.
     */
    public void update(PlatformIdentity id, boolean oldV, boolean newV) throws IllegalInputException, ConnectionFailedException {
    	if(oldV && !newV) {
    		for(AbstractConnection c : connections) {
    			if(c.getPlatformIdentity() == id) {
    				removeConnection(id);
    			}
    		}
    	}
    	else if(newV && !oldV) {
    		boolean isHere = false;
    		for(AbstractConnection c : connections) {
    			if(c.getPlatformIdentity() == id) {
    				isHere = true;
    			}
    		}
    		if(!isHere) {
    			switch(id) {
    			case PyBossa: {
    				additionalPublish(new PyBossaConnection());
    			}
    			case MTurk: {
    				additionalPublish(new MTurkConnection());
    			}
    			default: {
    				Logger.log("Tried to update to unknown Platform");
    				throw new IllegalInputException("Tried to update to unknown Platform", "");
    			}
    			}
    		}
    	}
    }
    
    /**
     * Publishes a HIT on all platforms linked to this Transformer. 
     * 
     * @param spec the HITSpec which shall be used for creating the HIT
     * @return the unified ID of all platforms.
     * @throws ConnectionFailedException if the connection to a platform cannot be established
     */
    public String publishHIT(HITSpec spec) throws ConnectionFailedException {
    	this.mySpec = spec;
        List<String> id = new ArrayList<String>();
        
        for(AbstractConnection currentConnection : connections) {
        	String currentID = currentConnection.publishHIT(spec);
            id.add("?" + currentConnection.getPlatformIdentity().toString() + "|" + currentID);
        }
        idAfterPublish = mediator.generateID(id);
        return idAfterPublish;
        
    }
    /**
     * ends the HIT on all platforms linked to the Transformer
     * 
     * @param hitID the MainID which will be split into the corresponding IDs
     * @throws IllegalInputException 
     */
    public void endHIT(String hitID) throws IllegalInputException {
    	if (hitID == null) {
    		throw new IllegalInputException("","");
    	}
        for(AbstractConnection currentConnection : connections) {
        	currentConnection.endHIT(mediator.getID(hitID + platformsAddedLater, currentConnection.getPlatformIdentity()));
        }


    }
    /**
     * Extends the assignmentNumbers on the platforms by consulting the internal
     * logic.
     * @param nrAsmnts the amount of increases.
     * @param hitID the mainID.
     * @throws IllegalInputException 
     */
    public void extendAssignmentNumber(int nrAsmnts, String hitID) throws IllegalInputException {
    	if (hitID == null) {
    		throw new IllegalInputException("","");
    	}
    	List<PlatformIdentity> platformNames = new ArrayList<>();
    	for(AbstractConnection c : connections) {
    		platformNames.add(c.getPlatformIdentity());
    	}
        Iterable<Integer> inc = mediator.loadDistribution(platformNames, nrAsmnts);
        Iterator<AbstractConnection> iter = connections.iterator();
        for(int i: inc) {
        	AbstractConnection c = iter.next();
        	c.extendAssignmentNumber(i, mediator.getID(hitID + platformsAddedLater, c.getPlatformIdentity()));
        }
    }
    /**
     * This method verifies whether the listed platforms have a sufficient budget
     * to start the Experiment.
     * 
     * @param amount the amount of monetary payment expected to be present.
     * @return whether the amount is present or not.
     * @throws ConnectionFailedException if the connection to one of the platforms is invalid.
     * @throws IllegalInputException if either platform is upset about the fact that the budget
     * is outside of the respective limits
     */
    public boolean checkBudget(int amount) throws ConnectionFailedException, IllegalInputException {
        List<Boolean> ok = new ArrayList<Boolean>();
        for(AbstractConnection currentConnection : connections) {
        	ok.add(currentConnection.checkBudget(amount));
        }

        return mediator.budgetVerification(ok);
        
    }
    /**
     * This method approves a HIT with feedback. The submethod getRealID is 
     * used for determining the correct Platform and  ID.
     * @param Id the raw AssigmentID.
     * @param feedback the feedback to be given.
     * @throws IllegalInputException why not.
     */
    public void approveHIT(AssignmentId Id, WorkerId workerId, String feedback) throws IllegalInputException {
    	if (Id == null || workerId == null || feedback == null) {
    		throw new IllegalInputException("","");
    	}
    	for(AbstractConnection currentConnection : connections) {
        	AssignmentId result = getRealID(currentConnection.getAbbrevation(), Id.getId());
        	if(result != null) {
        		currentConnection.approveHIT(result, workerId, feedback);
        		Logger.log("Approved HIT " + result.getId() + " at " +currentConnection.getPlatformIdentity().toString());
        	}

        	
        }
    }
    /**
     * This method sends a bonus payment to a worker, again the submethod getRealID
     * is used.  
     * @param idA The raw AssignmentID
     * @param idW the WorkerID
     * @param bonus the bonus in cents
     * @param message the message if applicable
     * @throws IllegalInputException the bonus cents should be in limits (i.e. >0)
     */
    public void sendBonus(AssignmentId idA, WorkerId idW, int bonus,String message) throws IllegalInputException {
    	if(idA == null || idW == null || message == null) {
    		throw new IllegalInputException("", "");
    	}
        for(AbstractConnection currentConnection : connections) {
        	
        	AssignmentId result = getRealID(currentConnection.getAbbrevation(), idA.getId());
        	if(result != null) {
        		currentConnection.sendBonus(result, idW, bonus, message);
        		Logger.log("Sent Bonus " + result.getId() + " to " +currentConnection.getPlatformIdentity().toString());
        	}

        	
        }

    }
    /**
     * Rejects an assignment.
     * @param id the raw id
     * @param reason the reason for rejecting
     * @throws IllegalInputException the reason may not be empty
     */
    public void rejectAssignment(AssignmentId id, String reason) throws IllegalInputException {
    	if (id == null || reason == null) {
    		throw new IllegalInputException("","");
    	}
    	for(AbstractConnection currentConnection : connections) {
        	AssignmentId result = getRealID(currentConnection.getAbbrevation(), id.getId());
        	if(result != null) {
        		currentConnection.rejectAssignment(result, reason);
        		Logger.log("rejected assignment " + result.getId() + " at " +currentConnection.getPlatformIdentity().toString());
        	}

        	
        }
    }
    
    /**
     * This method determines whether the ID of the Assignment contains the Identifier
     * to a certain platform.
     * 
     * @param platformAbbrevation the identifier of the Platform
     * @param idAssigment the Assignment
     * @return the AssignmentID without the Identifier if it is the correct platform
     * null otherwise
     */
    
    private AssignmentId getRealID(String platformAbbrevation, String idAssigment) {
    	if(platformAbbrevation == null) {
    		Logger.log("Abbrevation of Platform not present");
    		platformAbbrevation ="X" + idAssigment + "X";
    	}
    	if(idAssigment.indexOf(platformAbbrevation) == 0) {
    		String realID = idAssigment.substring(platformAbbrevation.length());
    		return new AssignmentId(realID);
    	}
    	return null;
    }
    
    
    
}
