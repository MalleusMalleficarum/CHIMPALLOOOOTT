package edu.kit.ipd.creativecrowd.controller;
import mockit.*;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;














import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.BeforeClass;

import spark.Request;
import spark.Response;
import spark.route.HttpMethod;
import spark.route.RouteMatch;
import edu.kit.ipd.creativecrowd.connector.Connector;
import edu.kit.ipd.creativecrowd.mutablemodel.ExperimentRepo;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.util.GlobalApplicationConfig;
import edu.kit.ipd.creativecrowd.view.MockExperiment;
import edu.kit.ipd.creativecrowd.view.ViewFacade;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.persistentmodel.PersistentExperimentRepo;
import edu.kit.ipd.creativecrowd.readablemodel.CreativeTask;
import edu.kit.ipd.creativecrowd.readablemodel.Experiment;
public class ExperimentControllerUnitTest {
	MockExperiment mexp;
	@Before
	public void setUp() {
		try {
			if(mexp == null){
				mexp = new MockExperiment(); 
			}
			//ex = mexp.getExperiment();
			
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
	}
	@Test
	public void createExperimentTest(){
		
		MockUp<Response> m = new MockUp<Response>()
		{
			public void status(int statusCode){
				
			}
		};
		MockUp<Request> a = 	new MockUp<Request>()
			{
				@Mock
				public void $init(){
					
				}
				@Mock
				public String headers(String header){
					return "application/json";
				}
				@Mock
				public String body(){
					return "{\"budget\": 500,\n"+
							"\"question\": \"Do a backflip\",\n"+
							"\"task_picture_url\": \"http:\\/\\/imgs.xkcd.com\\/comics\\/screenshot.png\",\n"+
							"\"task_picture_license_url\": \"http:\\/\\/xkcd.com\\/1373\\/\",\n"+
							"\"task_description\": \"This is the task description\",\n"+
							"\"hit_title\": \"This is the HIT title.\",\n"+
							"\"hit_description\": \"The HIT description\",\n"+
							"\"basicPaymentHIT\": 20,\n"+
							"\"basicPaymentAnswer\": 10,\n"+
							"\"bonusPayment\": 5,\n"+
							"\"basicPaymentRating\": 12,\n"+
							"\"maxAnswersPerAssignment\" : 2,\n"+
							"\"maxRatingsPerAssignment\" : 2,\n"+
							"\"ratingTaskQuestion\" : \"ratingTaskQgdfhdgjuestion\",\n"+
							"\"strategyParams\": {"+
								"\"tacc_class\": \"edu.kit.ipd.creativecrowd.operations.strategies.NAssignments\",\n"+
								"\"tacc.n\" : \"12\",\n"+


								"\"aqic_class\": \"edu.kit.ipd.creativecrowd.operations.strategies.AverageRating\",\n"+


								"\"apoc_class\": \"edu.kit.ipd.creativecrowd.operations.strategies.BonusAPOC\",\n"+
								"\"apoc.workers_to_receive_bonus\" : \"1\",\n"+
								"\"apoc.bonus_pool\" : \"0\",\n"+
								"\"apoc.weight_of_answer\" : \"3\",\n"+



								"\"rsd_class\": \"edu.kit.ipd.creativecrowd.operations.strategies.ConsentRatingDecider\",\n"+
								"\"rsd.quorum\": \"1\",\n"+
								"\"rsd.allowed_stddev\": \"1.0\",\n"+


								"\"rqic_class\": \"edu.kit.ipd.creativecrowd.operations.strategies.EnsureRatingDiversity\",\n"+
								"\"rqic.quorum\" : \"5\",\n"+
								"\"rqic.threshold\" : \"0.8\",\n"+
								"\"rqic.val_good\" : \"1.0\",\n"+
								"\"rqic.val_bad\" : \"0.0\",\n"+
								

								"\"ftg_class\": \"edu.kit.ipd.creativecrowd.operations.strategies.FeedbackWithoutRatingText\",\n"+
								"\"ftg.rejection_message\" : \"You suck\",\n"+
								"\"ftg.approval_message\" : \"You´re cool\",\n"+
								"\"ftg.bonus_granted_message\" : \"You got bonus\",\n"+

								"\"tcm_class\": \"edu.kit.ipd.creativecrowd.operations.strategies.FreeformTaskConstellationMutator\",\n"+
								"\"tcm.ratings_per_task\" : \"1\""+



							"},\n"+
							"\"ratingView_class\": \"edu.kit.ipd.creativecrowd.view.ThumbRatingView\""+
							"\"ratingOptions\": {"+
								"\"hallo\": 1.3,\n"+
								"\"String\": 2.0,\n"+
								"\"Pizza\" : 4.2"+
							"},\n"+
							"\"qualifications\": [ \"00000000000000000060,1,EqualTo,false\"],\n"+
							"\"tags\": [ \"lol\", \"konnotation\" ]}";
				}
				@Mock
				public String params(String param){
					return "Test2";
				}
				
			
		
		};
		MockUp<ViewFacade> vi = new MockUp<ViewFacade>(){
			@Mock 
			public String showStatus(Experiment ex){
				return "ID: Test\n"
						+ "Creative task: Do a backflip\n"
						+ "Tags for this experiment: [Pizza Kuchen Torte]\n";
				
			}
		};
		GlobalApplicationConfig.configureFromServletContext(null);

		Request sparkRequest = new MockRequest();
		Response resp = new MockResponse();
		
		ExperimentController cont = new ExperimentController();
		String x = cont.createExperiment(sparkRequest, resp).body();
	
		String hope = "ID: Test\n"
				+ "Creative task: Do a backflip\n"
				+ "Tags for this experiment: [Pizza Kuchen Torte]\n";
		assertTrue(x.contains(hope));
		System.out.println(x);
		m.tearDown();
		a.tearDown();
		vi.tearDown();
		try {
			ExperimentRepo rep = new PersistentExperimentRepo();
			rep.deleteExperiment("Test2");
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void getStatisticsTest(){
		
		MockUp<Response> m = new MockUp<Response>()
		{
			public void status(int statusCode){
				
			}
		};
		MockUp<Request> a =	new MockUp<Request>()
			{
				@Mock
				public void $init(){
					
				}
				@Mock
				public String headers(String header){
					return "application/json";
				}
				@Mock
				public String body(){
					return "";
				}
				@Mock
				public String params(String param){
					return "Test";
				}
				@Mock 
				public String pathInfo(){
					return "/experiment";
				}
				
				
			
		
		};
		Request sparkRequest = new MockRequest();
		Response resp = new MockResponse();
		ExperimentController cont = new ExperimentController();
		String hope = "ID: Test\n"
				+ "Creative task: Do a backflip\n"
				+ "Tags for this experiment: []\n";
		assertTrue(cont.getStatistics(sparkRequest, resp).body().contains(hope));
		m.tearDown();
		a.tearDown();
	}
	
	@Test
	public void endExperimentTest(){
		MockUp<Response> m = new MockUp<Response>()
		{
			public void status(int statusCode){
				
			}
		};
		MockUp<Request> a =	new MockUp<Request>()
			{
				@Mock
				public void $init(){
					
				}
				@Mock
				public String headers(String header){
					return "application/json";
				}
				@Mock
				public String body(){
					return "";
				}
				@Mock
				public String params(String param){
					return "Test";
				}
				@Mock 
				public String pathInfo(){
					return "/experiment";
				}
				
			
		
		};
		MockUp<Connector> c = new MockUp<Connector>(){
			@Mock
			public void endExperiment(String expId){
				
			}
		};
		Request sparkRequest = new MockRequest();
		Response resp = new MockResponse();
		ExperimentController cont = new ExperimentController();
		String hope = "successfully ended experiment with id Test\n";
		assertTrue(cont.endExperiment(sparkRequest, resp).body().contains(hope));
		m.tearDown();
		a.tearDown();
		c.tearDown();
	}
	@Test
	public void deleteExperimentTest(){
		MockUp<Connector> c = new MockUp<Connector>(){
			@Mock
			public void deleteExperiment(String expId){
				
			}
		};
		MockUp<Response> m = new MockUp<Response>()
				{
					public void status(int statusCode){
						
					}
				};
				MockUp<Request> a =	new MockUp<Request>()
			{
				@Mock
				public void $init(){
					
				}
				@Mock
				public String headers(String header){
					return "application/json";
				}
				@Mock
				public String body(){
					return "";
				}
				@Mock
				public String params(String param){
					return "Test";
				}
				
			
		
		};
		Request sparkRequest = new MockRequest();
		Response resp = new MockResponse();
		ExperimentController cont = new ExperimentController();
		String hope = "successfully deleted experiment with id Test\n";
		assertTrue(cont.deleteExperiment(sparkRequest, resp).body().contains(hope));
		m.tearDown();
		a.tearDown();
		c.tearDown();
	}
	@After
	public void cleanUp(){
//		File file = new File("CreativeCrowd.db");
//		file.delete();
		}
	

	
}
