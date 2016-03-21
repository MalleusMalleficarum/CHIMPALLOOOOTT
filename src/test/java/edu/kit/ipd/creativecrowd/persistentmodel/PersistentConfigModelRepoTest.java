package edu.kit.ipd.creativecrowd.persistentmodel;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.kit.ipd.chimpalot.jsonclasses.ConfigModelJson;
import edu.kit.ipd.chimpalot.util.GlobalApplicationConfig;
import edu.kit.ipd.creativecrowd.readablemodel.ConfigModel;
/**
 * 
 * Tests the Repository connection, results in weird stuff if the database is dirty.
 * @author Robin
 *
 */
public class PersistentConfigModelRepoTest {
	PersistentConfigModelRepo repo;
	ConfigModelJson mod1;
	ConfigModelJson mod2;
	@BeforeClass
	public static void setUpBeforeClass() {
		File file = new File("CreativeCrowd.db");
		file.delete();

	}

	@Before
	public void setUp() {
		File file = new File("CreativeCrowd.db");
		file.delete();
		GlobalApplicationConfig.configure(true);
		try {
			repo = new PersistentConfigModelRepo();
			
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}

		mod1 = new ConfigModelJson();
		mod1.setID("VampireSlayer");
		mod2 = new ConfigModelJson();
		mod2.setID("SteinGegenKopf");
	}

	@After
	public void tearDown()  {
		
		File file = new File("CreativeCrowd.db");
		file.delete();
	}
	//TODO THIS
	@Test 
	public void properAmountOfConfigModels() {
		try {
			int i = repo.loadAllConfigModels().size();
			assertEquals(repo.loadAllConfigModels().size(), i);
			repo.createConfigModel(mod1, mod1.getID(), null);
			assertEquals(repo.loadAllConfigModels().size(), i + 1);
			repo.deleteConfigModel(mod1.getID());
			assertEquals(repo.loadAllConfigModels().size(), i);
		}  catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	//TODO THIS
	@Test (expected = IDAlreadyUsedException.class)
	public void doubleTheFun() throws IDAlreadyUsedException, DatabaseException {
		
		repo.createConfigModel(mod1, mod1.getID(), null);
		repo.createConfigModel(mod1, mod1.getID(), null);
		
	}
	//TODO this
	@Test 
	public void iCanUseTheSameWithDiffIDS() {
		try {
			int i = repo.loadAllConfigModels().size();
			assertEquals(repo.loadAllConfigModels().size(), i);
			repo.createConfigModel(mod1, "hallo", null);
			assertEquals(repo.loadAllConfigModels().size(), i + 1);
			repo.createConfigModel(mod1, "muh", null);
			assertEquals(repo.loadAllConfigModels().size(), i + 2);
			repo.deleteConfigModel("hallo");
			assertEquals(repo.loadAllConfigModels().size(), i + 1);
			repo.deleteConfigModel("muh");
			assertEquals(repo.loadAllConfigModels().size(), i);
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}

	}
	@Test
	public void properExperimentID() {
		try {
			ConfigModel mod = repo.createConfigModel(mod1, "ma", "12");
			assertEquals(mod.getExperimentID(), "12");
			repo.deleteConfigModel("ma");
		}  catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
}
