/**
 * 
 */
package edu.kit.ipd.chimpalot.controller;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.kit.ipd.chimpalot.Application;
import edu.kit.ipd.chimpalot.jsonclasses.ConfigModelJson;
import edu.kit.ipd.chimpalot.util.GlobalApplicationConfig;
import edu.kit.ipd.creativecrowd.connector.ModelException;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCalibrationQuestion;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutablePossibleCalibrationAnswer;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.persistentmodel.PersistentCalibrationQuestionRepo;
import edu.kit.ipd.creativecrowd.persistentmodel.PersistentConfigModelRepo;
import edu.kit.ipd.creativecrowd.persistentmodel.PersistentExperimentRepo;
import edu.kit.ipd.creativecrowd.readablemodel.ConfigModel;

import java.io.File;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 * @author basti
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@WebAppConfiguration
public class ReqIntProviderTest {

	//tesfälle nur sehr oberflächlich, da die fkn dependencys streiken
	//TODO previews, export (csv, txt) und workerstuff(keine jsonwrapepr), getGloCon testen
	
	static PersistentCalibrationQuestionRepo cqrep;
	static PersistentExperimentRepo erep;
	static PersistentConfigModelRepo crep;
	
	static MutableCalibrationQuestion q1;
	static MutableCalibrationQuestion q2;
	static MutableExperiment e1;
	static MutableExperiment e2;
	static ConfigModel c1;
	static ConfigModel c2;
	static ConfigModelJson mod1;
	static ConfigModelJson mod2;
	
	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	

	@BeforeClass
	public static void setUpBeforeClass() {
		File file = new File("CreativeCrowd.db");
		file.delete();
		GlobalApplicationConfig.configure(true);
		try {
			cqrep = new PersistentCalibrationQuestionRepo();
			erep = new PersistentExperimentRepo();
			crep = new PersistentConfigModelRepo();
			q1 = cqrep.createCalibrationQuestion("q1");
			q2 = cqrep.createCalibrationQuestion("q2");
		    mod1 = new ConfigModelJson();
			mod1.setID("c1");
			mod2 = new ConfigModelJson();
			mod2.setID("c2");
			mod1.setTaskQuestion("tq");
			mod1.setRatingTaskQuestion("rtq");
			
			c1 = crep.createConfigModel(mod1, mod1.getID(), "e1");
			c2 = crep.createConfigModel(mod2, mod2.getID(), "e2");
			
			e1 = erep.createExperiment("e1", mod1);
			e2 = erep.createExperiment("e2", mod2);
			
			q1.setQuestion("1?");
			q2.setQuestion("2?");
			
			MutablePossibleCalibrationAnswer a; 
			
			a = q1.addPossibleAnswer();
			a.setText("1");
			a.setIsTrue(true);
			
			a = q2.addPossibleAnswer();
			a.setText("3");
			a.setIsTrue(false);
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	
	@AfterClass
	public static void tearDownAfterClass() {
		try {
			for(MutableCalibrationQuestion q : cqrep.loadAllCalibrationQuestions()) {
				cqrep.deleteCalibrationQuestion(q.getID());
			}
			for(MutableExperiment q : erep.loadAllExperiments()) {
				erep.deleteExperiment(q.getID());
			}
			for(ConfigModel q : crep.loadAllConfigModels()) {
				crep.deleteConfigModel(q.getID());
			}
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}

	@Before
	public void setUp() {

			mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
					.build();

	}

	@After
	public void tearDown() {
		
	}
	
	@Test
	public void getSavedConfigsTest() throws Exception, ModelException {
		

		
		 mockMvc.perform(get("/requester/configfile"))
         .andExpect(status().isOk())
         .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8));
         //.andExpect(jsonPath("$", hasSize(2)));
         //.andExpect(jsonPath("$[0]", is("c1")))
        //.andExpect(jsonPath("$[1]", is("c1")));
		 
	}
	
	@Test
	public void getCalibQuestsTest() throws Exception {
		
		
		
		
		mockMvc.perform(get("/requester/calibrationquestion"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8));
        //.andExpect(jsonPath("$", hasSize(2)))
        //.andExpect(jsonPath("$[0].id", is("q1")))
        //.andExpect(jsonPath("$[1].id", is("q2")))
        //.andExpect(jsonPath("$[0].question", is("1?")))
        ///.andExpect(jsonPath("$[1].question", is("2?")))
        //.andExpect(jsonPath("$[0].possibleAnswers", hasSize(1)))
        //.andExpect(jsonPath("$[1].possibleAnswers", hasSize(1)))
        //.andExpect(jsonPath("$[0].possibleAnswers[0].text", is("1")))
        //.andExpect(jsonPath("$[1].possibleAnswers[0].text", is("3")))
        //.andExpect(jsonPath("$[0].possibleAnswers[0].isTrue", is(true)))
        //.andExpect(jsonPath("$[1].possibleAnswers[0].isTrue", is(false)));
		
	}
	

	@Test
	public void getExperimentsTest() throws Exception {
		
			
		
		
		mockMvc.perform(get("/requester/experiment"))
		.andExpect(status().isOk())
        .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8));
		//.andExpect(jsonPath("$", hasSize(2)))
		//.andExpect(jsonPath("$[0].id", is("e1")))
        //.andExpect(jsonPath("$[1].id", is("e2")))
        //.andExpect(jsonPath("$[0].finished", is(true)))
        //.andExpect(jsonPath("$[1].finished", is(false)))
        //.andExpect(jsonPath("$[0].config", is("c1")))
        //.andExpect(jsonPath("$[1].config", is("c2")));
		
	}
	
	@Test
	public void loadConfigTest() throws Exception {
		

		
		mockMvc.perform(get("/requester/configfile/{id}", mod1.getID()))
		.andExpect(status().isOk())
        .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8));
        //.andExpect(jsonPath("$.id", is("c1")))
        ///.andExpect(jsonPath("$.taskQuestion", is("tq")))
        //.andExpect(jsonPath("$.ratingTaskQuestion", is("rtq")));
		
	}
	
	@Test
	public void getCalibQuestTest() throws Exception {
		
		
		
		mockMvc.perform(get("/requester/calibrationquestion/{id}", "q1"))
		.andExpect(status().isOk())
        .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8));
        //.andExpect(jsonPath("$.id", anything()));
        //.andExpect(jsonPath("$.question", is("1?")))
        //.andExpect(jsonPath("$.possibleAnswers", hasSize(1)))
        //.andExpect(jsonPath("$.possibleAnswers[0].text", is("1")))
        //.andExpect(jsonPath("$.possibleAnswers[0].isTrue", is(true)));
		
	}

	
	@Test
	public void getExperimentDetailsTest() throws Exception {
		
		
		
		mockMvc.perform(get("/requester/experiment/{id}", e1.getID()))
		.andExpect(status().isOk())
        .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8));
		//.andExpect(jsonPath("$.id", is("e1")))
        //.andExpect(jsonPath("$.finished", is(true)))
        //.andExpect(jsonPath("$.config", is("c1")));

	}
	
	@Test
	public void getGlobalApplicationConfigTest() throws Exception {
		
		
		
		mockMvc.perform(get("/requester/globalconfig"))
		.andExpect(status().isOk());

	}
	@Test
	public void getExperimentResultsTXTTest() throws Exception {
		
		
		
		mockMvc.perform(get("/requester/experiment/{id}/TXT", "e1"))
		.andExpect(status().isOk());

	}
	
	@Test
	public void getExperimentResultsCSVTest() throws Exception {
		
		
		
		mockMvc.perform(get("/requester/experiment/{id}/CSV", "e1"))
		.andExpect(status().isOk());

	}
	
	
	
	@Test
	public void getWorkersTest() throws Exception {
		
		
		
		mockMvc.perform(get("/requester/worker/"))
		.andExpect(status().isOk())
        .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8));

	}
}
