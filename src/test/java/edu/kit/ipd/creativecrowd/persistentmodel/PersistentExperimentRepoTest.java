package edu.kit.ipd.creativecrowd.persistentmodel;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.kit.ipd.chimpalot.jsonclasses.ConfigModelJson;
import edu.kit.ipd.chimpalot.util.GlobalApplicationConfig;
import edu.kit.ipd.chimpalot.util.Logger;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.ConfigModel;
/**
 * 
 * @author the unknown author + Robin
 *
 */
public class PersistentExperimentRepoTest {
	static PersistentExperimentRepo testRep;
	ConfigModel model;

	@BeforeClass
	public static void setupBeforeClass(){
		GlobalApplicationConfig.configure(true);
		try {
			File file = new File("CreativeCrowd.db");
			file.delete();
			testRep = new PersistentExperimentRepo();
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}
	@Before
	public void setUp() {
		ConfigModelJson c = new ConfigModelJson();
		c.setID(testRep.connection.generateID("configmodel"));
		try {
			model = new PersistentConfigModelRepo().createConfigModel(c, c.getID(), null);
			Logger.debug(model.getID());
		}catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@After
	public void tearDown() {
		Logger.debug("==================================");
		try {
			List<MutableExperiment> list = testRep.loadAllExperiments();
			for(MutableExperiment e : list) {
				testRep.deleteExperiment(e.getID());
				new PersistentConfigModelRepo().deleteConfigModel(model.getID());
			}
			
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	
	@Test (expected = IDAlreadyUsedException.class)
	public void noDoubleIDs() throws IDAlreadyUsedException, DatabaseException {
			Logger.debug(model.getID());
			testRep.createExperiment("hallo", model);
			testRep.createExperiment("hallo", model);


	}
	/**
	 * I expect to get the same experiment
	 */
	@Test
	public void createAndLoad() {
		try {
			MutableExperiment e = testRep.createExperiment("Native Faith", model);
			e.setHitID("This is a comparison ID");
			MutableExperiment comp = testRep.loadExperiment(e.getID());
			assertEquals(e.getHitID(), comp.getHitID());
			Logger.debug("********** " +comp.getID());
			assertEquals(comp.getID(), e.getID());
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test (expected = DatabaseException.class)
	public void createNull() throws IDAlreadyUsedException, DatabaseException {
		testRep.createExperiment(null, model);
	}
	@Test (expected = DatabaseException.class)
	public void loadNull() throws IDNotFoundException, DatabaseException {
		testRep.loadExperiment(null);
	}
	@Test (expected = IDNotFoundException.class)
	public void loadSomethingNotThere() throws IDNotFoundException, DatabaseException {
		testRep.loadExperiment("Heartfelt Fantasy");
	}
	@SuppressWarnings("rawtypes")
	private int length(Iterable iter) {
		int i = 0;
		for(@SuppressWarnings("unused") Object o : iter) {
			i++;
		}
		return i;
	}
	@Test
	public void loadAllButNeitherThere() {
		try {
			assertEquals(length(testRep.loadAllExperiments()), 0);
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test (expected = DatabaseException.class)
	public void deleteNull() throws IDAlreadyUsedException, DatabaseException {

		testRep.deleteExperiment(null);

	}
	@Test (expected = DatabaseException.class) 
	public void createDeleteLoad() throws DatabaseException {
		MutableExperiment e = testRep.createExperiment("Legendary Fish", model);
		testRep.deleteExperiment(e.getID());
		testRep.loadExperiment(e.getID());
	}
	@Test
	public void loadLoadAndLoad() {
		
		try {
			assertEquals(length(testRep.loadAllExperiments()), 0);
			testRep.createExperiment("Lunate Elf", model);
			assertEquals(length(testRep.loadAllExperiments()), 1);
			testRep.createExperiment("Beloved Tomboyish Girl", model);
			assertEquals(length(testRep.loadAllExperiments()), 2);
			
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test (expected = DatabaseException.class)
	public void noNullConfig() throws IDAlreadyUsedException, DatabaseException {
		testRep.createExperiment("ich bin zwar nich null", null);
	}
	@Test
	public void loadBlank() {
		try {
			
			testRep.createExperiment("haöö", model);
			testRep.loadExperiment("haöö");

		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}

	
}
