package edu.kit.ipd.creativecrowd.transformer;

import org.junit.Test;

import edu.kit.ipd.creativecrowd.crowdplatform.ConnectionFailedException;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
public class ConfigFileInterpreterTest {
	
	@Test(expected = ConnectionFailedException.class)
	public void testInvalidExperiment() throws DatabaseException, ConnectionFailedException {
		ConfigFileInterpreter.getTransformer(null);
	}

}
