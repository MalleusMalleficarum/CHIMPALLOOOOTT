/**
 * 
 */
package edu.kit.ipd.creativecrowd.controller;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.kit.ipd.chimpalot.jsonclasses.ConfigModelJson;
import edu.kit.ipd.chimpalot.util.GlobalApplicationConfig;
import edu.kit.ipd.creativecrowd.connector.Connector;
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
import java.util.Arrays;
 
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 * @author basti
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/web.xml")
@WebAppConfiguration
public class ReqIntProviderTest {

	//TODO previews, export (csv, txt) und workerstuff(keine jsonwrapepr), getGloCon testen
	
	static PersistentCalibrationQuestionRepo cqrep;
	static PersistentExperimentRepo erep;
	static PersistentConfigModelRepo crep;
	
	MutableCalibrationQuestion q1;
	MutableCalibrationQuestion q2;
	MutableExperiment e1;
	MutableExperiment e2;
	ConfigModel c1;
	ConfigModel c2;
	ConfigModelJson mod1;
	ConfigModelJson mod2;
	
	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Autowired
	private Connector connector;
	
	@BeforeClass
	public static void setUpBeforeClass() {
		File file = new File("CreativeCrowd.db");
		file.delete();
		GlobalApplicationConfig.configure(true);
		try {
			cqrep = new PersistentCalibrationQuestionRepo();
			erep = new PersistentExperimentRepo();
			crep = new PersistentConfigModelRepo();
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	
	@AfterClass
	public static void tearDownAfterClass() {
	}

	@Before
	public void setUp() {
		try {
			mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
					.build();
			
			q1 = cqrep.createCalibrationQuestion("q1");
			q2 = cqrep.createCalibrationQuestion("q2");
		    mod1 = new ConfigModelJson();
			mod1.setID("c1");
			mod2 = new ConfigModelJson();
			mod2.setID("c2");
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

	@After
	public void tearDown() {
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
	
	@Test
	public void getSavedConfigsTest() throws Exception, ModelException {
		
		when(connector.getConfigList()).thenReturn(Arrays.asList(mod1, mod2));
		
		 mockMvc.perform(get("/requester/configfile"))
         .andExpect(status().isOk())
         .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
         .andExpect(jsonPath("$", hasSize(2)))
         .andExpect(jsonPath("$[0]", is("1")))
         .andExpect(jsonPath("$[1]", is("2")));
		 
		 verify(connector, times(1)).getConfigList();
	        verifyNoMoreInteractions(connector);
	}
	
	@Test
	public void getCalibQuestsTest() throws Exception {
		
		
		
		when(connector.getCalibList()).thenReturn(Arrays.asList(q1, q2));
		
		mockMvc.perform(get("/requester/calibrationquestion"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].id", is("q1")))
        .andExpect(jsonPath("$[1].id", is("q2")))
        .andExpect(jsonPath("$[0].question", is("1?")))
        .andExpect(jsonPath("$[1].question", is("2?")))
        .andExpect(jsonPath("$[0].possibleAnswers", hasSize(1)))
        .andExpect(jsonPath("$[1].possibleAnswers", hasSize(1)))
        .andExpect(jsonPath("$[0].possibleAnswers[0].text", is("1")))
        .andExpect(jsonPath("$[1].possibleAnswers[0].text", is("3")))
        .andExpect(jsonPath("$[0].possibleAnswers[0].isTrue", is(true)))
        .andExpect(jsonPath("$[1].possibleAnswers[0].isTrue", is(false)));
		
		verify(connector, times(1)).getCalibList();
        verifyNoMoreInteractions(connector);
	}
	

	@Test
	public void getExperimentsTest() throws Exception {
		
		when(e1.isFinished()).thenReturn(true);
		when(e2.isFinished()).thenReturn(false);
		
		when(e1.getStats()).thenReturn(null);
		when(e2.getStats()).thenReturn(null);
			
		when(connector.getExperimentList()).thenReturn(Arrays.asList(e1, e2));
		
		mockMvc.perform(get("/requester/experiment"))
		.andExpect(status().isOk())
        .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
		.andExpect(jsonPath("$", hasSize(2)))
		.andExpect(jsonPath("$[0].id", is("e1")))
        .andExpect(jsonPath("$[1].id", is("e2")))
        .andExpect(jsonPath("$[0].finished", is(true)))
        .andExpect(jsonPath("$[1].finished", is(false)))
        .andExpect(jsonPath("$[0].config", is("c1")))
        .andExpect(jsonPath("$[1].config", is("c2")));
		
		verify(connector, times(1)).getExperimentList();
        verifyNoMoreInteractions(connector);
	}
	
	@Test
	public void loadConfigTest() throws Exception {
		
		mod1.setTaskQuestion("tq");
		mod1.setRatingTaskQuestion("rtq");
		
		when(connector.getConfigModel(mod1.getID())).thenReturn(mod1);
		
		mockMvc.perform(get("/requester/configfile{" + mod1.getID() + "}"))
		.andExpect(status().isOk())
        .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.id", is("c1")))
        .andExpect(jsonPath("$.taskQuestion", is("tq")))
        .andExpect(jsonPath("$.ratingTaskQuestion", is("rtq")));
		
		verify(connector, times(1)).getConfigModel(mod1.getID());
        verifyNoMoreInteractions(connector);
	}
	
	@Test
	public void getCalibQuestTest() throws Exception {
		
		when(connector.getCalibrationQuestion(q1.getID())).thenReturn(q1);
		
		mockMvc.perform(get("/requester/calibrationquestion/{" + q1.getID() + "}"))
		.andExpect(status().isOk())
        .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.id", is("q1")))
        .andExpect(jsonPath("$.question", is("1?")))
        .andExpect(jsonPath("$.possibleAnswers", hasSize(1)))
        .andExpect(jsonPath("$.possibleAnswers[0].text", is("1")))
        .andExpect(jsonPath("$.possibleAnswers[0].isTrue", is(true)));
		
		verify(connector, times(1)).getCalibrationQuestion(q1.getID());
        verifyNoMoreInteractions(connector);
	}

	
	@Test
	public void getExperimentDetailsTest() throws Exception {
		
		when(e1.isFinished()).thenReturn(true);
		
		when(e1.getStats()).thenReturn(null);
		
		when(connector.getExperiment(e1.getID())).thenReturn(e1);
		
		mockMvc.perform(get("/requester/experiment/{" + e1.getID() + "}"))
		.andExpect(status().isOk())
        .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
		.andExpect(jsonPath("$.id", is("e1")))
        .andExpect(jsonPath("$.finished", is(true)))
        .andExpect(jsonPath("$.config", is("c1")));
		
		verify(connector, times(1)).getExperiment(e1.getID());
        verifyNoMoreInteractions(connector);
	}
	
}
