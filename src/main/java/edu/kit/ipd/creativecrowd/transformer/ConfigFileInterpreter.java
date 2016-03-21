package edu.kit.ipd.creativecrowd.transformer;

import edu.kit.ipd.creativecrowd.crowdplatform.ConnectionFailedException;
import edu.kit.ipd.creativecrowd.crowdplatform.MTurkConnection;
import edu.kit.ipd.creativecrowd.crowdplatform.PlatformIdentity;
import edu.kit.ipd.creativecrowd.crowdplatform.PyBossaConnection;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.Experiment;
/**
 * This class returns the transformer which can publish the results.
 * It resembles the Facade design and allows the rest of the application to get an 
 * adequate Transformer. (Note, this is in a static context, because currently the
 * behaviour is simple and fixed. If either of those changes feel free to adapt this class)
 * 
 * @author Robin
 *
 */
public final class ConfigFileInterpreter {
	//Again, we do not need instanciation of this class.
	private ConfigFileInterpreter() {

		
	}
	/**
	 * This method constructs the Transformer for the given Experiment. This method
	 * will always construct the same Transformer for the same experiment. 
	 * 
	 * @param e the Experiment
	 * @return the Transformer which is specified in the Configuraion of the Experiment
	 * @throws DatabaseException if it is not possible to access the Experiment
	 * @throws ConnectionFailedException it it is impossible to establish a connection to either
	 * of the resources.
	 */
	public static Transformer getTransformer(Experiment e) throws DatabaseException, ConnectionFailedException {
		if (e == null) {
			throw new ConnectionFailedException("Experiment may not be null.");
		}
		boolean mturk = e.getConfig().getSendCreativeTo(PlatformIdentity.MTurk);
		boolean pybossa = e.getConfig().getSendCreativeTo(PlatformIdentity.PyBossa);
		InternalLogicMediator mediator = new InternalLogicMediator();
		Transformer t = new Transformer(mediator);
		if(mturk) {
			t.addConnection(new MTurkConnection());
			
		}
		if(pybossa) {
			t.addConnection(new PyBossaConnection());
		}
		return t; 
	}
}
