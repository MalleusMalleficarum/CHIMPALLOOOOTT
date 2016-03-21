package edu.kit.ipd.creativecrowd.router;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import mockit.Mock;
import mockit.MockUp;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import spark.Request;
import spark.Response;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.PutMethodWebRequest;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;

import edu.kit.ipd.creativecrowd.controller.AssignmentController;
import edu.kit.ipd.creativecrowd.controller.ExperimentController;
import edu.kit.ipd.creativecrowd.util.Logger;

public class RouterTest {
	static ServletUnitClient sc;
	static ServletRunner sr;
	MockUp mocki [] = new MockUp[5];
	
	//Startet das Programm
	@BeforeClass
	public static void setUpClass() throws IOException, SAXException
	{
	    sr = new ServletRunner(new File("./src/test/webapp/WEB-INF/web.xml"));
	    sc = sr.newClient();
	}
	
	@Before
	public void setUp(){
		
	}
	
	@After
	public void tearDown(){
		for(MockUp mock: mocki)
		{
			if(mock!=null){
				if(mock!=null){
					try{
						mock.tearDown();
					}catch(NullPointerException e)
					{
						Logger.log("Leider unterstützt Ihre Java Umgebung keine funktionierende Abfrage/Tests, ob ein Objekt Null ist. Installieren Sie eine andere Umgebung oder starten Sie das Programm mit der Konsole");
					}
				}
			}
		}
	}
		
		/*
		 * Testet die TestRoute helloWorld
		 */
		@Test
		public void helloWorld() throws IOException, SAXException
		{		
		    WebRequest request   = new GetMethodWebRequest( "http://example.org/" );
		    WebResponse response = sc.getResponse( request );
		    assertNotNull( "No response received", response );
		    assertEquals( "content type", "text/plain", response.getContentType() );
		    assertEquals( "requested resource", "hallo welt!", response.getText() );
		}
		/*
		 * Testet die Route deleteExperiment
		 */
		@Test
		public void deleteExperiment() throws IOException, SAXException
		{	
			String URL = "http://localhost:4567/experiments/" + "test" + "/delete";
			mocki[0] = 	new MockUp<ExperimentController>(){
				@Mock
				public Response deleteExperiment(Request sparkRequest, Response resp){
					 resp.body(sparkRequest.url() + "t1");
					 return resp;
				}
			};
			WebRequest request = new GetMethodWebRequest(URL);
			WebResponse response = sc.getResponse(request);
			assertTrue(response.getText().contains(URL));	
			assertTrue(response.getText().contains("t1"));
		}
		
		/*
		 * Testet die Route createExperiment
		 */
		@Test
		public void createExperiment() throws IOException, SAXException
		{
			String URL = "http://localhost:4567/experiments/" + "test";
			mocki[0] = 	new MockUp<ExperimentController>(){
				@Mock
				public Response createExperiment(Request sparkRequest, Response resp){
					resp.body(sparkRequest.url() + "t2");
					 return resp;
				}
			};
			FileInputStream fis;
			fis = new FileInputStream(new File("./src/test/resources/config.json"));
			WebRequest  request = new PutMethodWebRequest(URL, fis, "application/json");
			WebResponse response = sc.getResponse(request);
			assertTrue(response.getText().contains(URL));	
			assertTrue(response.getText().contains("t2"));
		}
		
		/*
		 * Testet die Route getStatisticsTXT
		 */
		@Test
		public void getStatisticsTXT() throws IOException, SAXException
		{
			String URL = "http://localhost:4567/experiments/" + "test" + "/txt";
			mocki[0] = 	new MockUp<ExperimentController>(){
				@Mock
				public Response getStatistics(Request sparkRequest, Response resp){
					resp.body(sparkRequest.url() + "t3");
					 return resp;
				}
			};
		    WebRequest requestStatistic = new GetMethodWebRequest(URL);
		    WebResponse responseStatistic = sc.getResponse( requestStatistic );
			assertTrue(responseStatistic.getText().contains(URL));
			assertTrue(responseStatistic.getText().contains("t3"));
		}
		
		/*
		 * Testet die Route getStatisticsCSV
		 */
		@Test
		public void getStatisticsCSV() throws IOException, SAXException
		{
			String URL = "http://localhost:4567/experiments/" + "test" + "/csv";
			mocki[0] = 	new MockUp<ExperimentController>(){
				@Mock
				public Response getStatistics(Request sparkRequest, Response resp){
					resp.body(sparkRequest.url() + "t4");
					 return resp;
				}
			};
		    WebRequest requestStatistic = new GetMethodWebRequest(URL);
		    WebResponse responseStatistic = sc.getResponse( requestStatistic );
			assertTrue(responseStatistic.getText().contains(URL));
			assertTrue(responseStatistic.getText().contains("t4"));
		}
		
		/*
		 * Testet die Route getStatistics
		 */
		@Test
		public void getStatistics() throws IOException, SAXException
		{
			String URL = "http://localhost:4567/experiments/" + "test";
			mocki[0] = 	new MockUp<ExperimentController>(){
				@Mock
				public Response getStatistics(Request sparkRequest, Response resp){
					resp.body(sparkRequest.url() + "t5");
					 return resp;
				}
			};
		    WebRequest requestStatistic = new GetMethodWebRequest(URL);
		    WebResponse responseStatistic = sc.getResponse( requestStatistic );
			assertTrue(responseStatistic.getText().contains(URL));
			assertTrue(responseStatistic.getText().contains("t5"));
		}
		
		/*
		 * Testet die Route endExperiment
		 */
		@Test
		public void endExperiment() throws IOException, SAXException
		{
			String URL = "http://localhost:4567/experiments/" + "test" + "/end";
			mocki[0] = 	new MockUp<ExperimentController>(){
				@Mock
				public Response endExperiment(Request sparkRequest, Response resp){
					resp.body(sparkRequest.url() + "t6");
					 return resp;
				}
			};
		    WebRequest request = new PostMethodWebRequest(URL);
		    WebResponse response = sc.getResponse(request);
			assertTrue(response.getText().contains(URL));
			assertTrue(response.getText().contains("t6"));
		}
		
		/*
		 * Testet die Route showAssignment
		 */
		@Test
		public void showAssignment() throws IOException, SAXException
		{
			String URL = "http://localhost:4567/assignment/" + "test";
			mocki[0] = 	new MockUp<AssignmentController>(){
				@Mock
				public Response show(Request sparkRequest, Response resp){
					resp.body(sparkRequest.url() + "t7");
					 return resp;
				}
			};
		    WebRequest request = new GetMethodWebRequest(URL);
		    WebResponse response = sc.getResponse(request);
			assertTrue(response.getText().contains(URL));
			assertTrue(response.getText().contains("t7"));
		}
		
		/*
		 * Testet die Route updateAssignment
		 */
		@Test
		public void updateAssignment() throws IOException, SAXException
		{
			String URL = "http://localhost:4567/assignment/" + "test";
			mocki[0] = 	new MockUp<AssignmentController>(){
				@Mock
				public Response update(Request sparkRequest, Response resp){
					resp.body(sparkRequest.url() + "t8");
					 return resp;
				}
			};
		    WebRequest request = new PostMethodWebRequest(URL);
		    WebResponse response = sc.getResponse(request);
			assertTrue(response.getText().contains(URL));
			assertTrue(response.getText().contains("t8"));
		}
		
		/*
		 * Testet die Route submitAssignment
		 */
		@Test
		public void submitAssignment() throws IOException, SAXException{
			String URL = "http://localhost:4567/assignment/" + "test";
			mocki[0] = 	new MockUp<AssignmentController>(){
				@Mock
				public Response update(Request sparkRequest, Response resp){
					resp.body(sparkRequest.url() + "t9");
					 return resp;
				}
			};
		    WebRequest request = new PostMethodWebRequest(URL);
		    WebResponse response = sc.getResponse(request);
			assertTrue(response.getText().contains(URL));
			assertTrue(response.getText().contains("t9"));
		}
		
		/*
		 * Testet die Route start
		 */
		@Test
		public void start()
		{
			Router router = new Router();
			Router router2 = new Router();
			assertEquals(router.getInstance(), router2.getInstance());
		}
}
