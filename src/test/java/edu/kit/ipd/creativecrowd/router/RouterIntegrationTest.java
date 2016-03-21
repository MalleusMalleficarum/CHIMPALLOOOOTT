package edu.kit.ipd.creativecrowd.router;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.PutMethodWebRequest;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;

import edu.kit.ipd.creativecrowd.mturk.HITSpec;
import edu.kit.ipd.creativecrowd.util.TestWithFreshDB;

import java.io.FileInputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class RouterIntegrationTest extends TestWithFreshDB {
	static ServletUnitClient sc;
	static ServletRunner sr;
	String experimentName;
	WebRequest request;
	/*
	*Startet das Programm
	 */
	@BeforeClass
	public static void setUp() throws IOException, SAXException
	{
	    sr = new ServletRunner(new File("./src/test/webapp/WEB-INF/web.xml"));
	    sc = sr.newClient();
	}
	

	/*
	*Erstellt ein neues Experiment und testet ob es richtig angelegt wurde
	*/
	@Before
	public void setUpNewExperiment() throws IOException, SAXException {
		Calendar cal = GregorianCalendar.getInstance();
		experimentName = cal.getTime().toString().replace(" ", "");
		String createPath = "http://localhost:4567/experiments/" + experimentName;
		FileInputStream fis;
		fis = new FileInputStream(new File("./src/test/resources/config.json"));
	    request = new PutMethodWebRequest(createPath, fis, "application/json");
	    WebResponse response = sc.getResponse( request );
	    assertNotNull( "No response received", response );
	    assertEquals( "content type", "text/html", response.getContentType() );
	    assertTrue(response.getText().contains( experimentName));
	    assertTrue(response.getText().contains("0 workers accepted the HIT but did not submit anything"));
	    assertTrue(response.getText().contains("0 workers submitted the hit"));
	    assertTrue(response.getText().contains("0 ratings were given in total"));
	    assertTrue(response.getText().contains("0 workers previewed the HIT"));
	}
	/*
	*Löscht das Alte Experiment
	*/
	@After
	public void cleanDataSet() throws IOException, SAXException
	{
		WebRequest request = new GetMethodWebRequest("http://localhost:4567/experiments/" + experimentName + "/delete");
		WebResponse response = sc.getResponse(request);
	    assertNotNull( "No response received", response );
	    assertEquals( "content type", "text/plain", response.getContentType() );
	    assertTrue(response.getText().contains("successfully deleted experiment with id " + experimentName));
	}
	
	@AfterClass
	public static void stopServer()
	{
		sr.shutDown();
		sc.clearProxyServer();
		sc.clearContents();
	}
	
	/*
	*Testet ob der Server gestartet wurde und die Routen funktionieren
	*/
	@Test
	public void testHelloWorld() throws IOException, SAXException {
	    WebRequest request   = new GetMethodWebRequest( "http://example.org/" );
	    WebResponse response = sc.getResponse( request );
	    assertNotNull( "No response received", response );
	    assertEquals( "content type", "text/plain", response.getContentType() );
	    assertEquals( "requested resource", "hallo welt!", response.getText() );
	}
	/*
	*Testet ob das Experiment erfolgreich gelöscht wurde
	*/
	@Test
	public void testDelete() throws IOException, SAXException{ 
		//Deletes the old experiment
		WebRequest request1 = new GetMethodWebRequest("http://localhost:4567/experiments/" + experimentName + "/delete");
		WebResponse response1 = sc.getResponse(request1);
		//Creates a new one with the old name should not work if the experiment was not deleted correct
		String createPath = "http://localhost:4567/experiments/" + experimentName;
		FileInputStream fis;
		fis = new FileInputStream(new File("./src/test/resources/config.json"));
	    request = new PutMethodWebRequest(createPath, fis, "application/json");
	    WebResponse response = sc.getResponse( request );
	    assertNotNull( "No response received", response );
	    assertEquals( "content type", "text/html", response.getContentType() );
	}
	
	/*
	*Testet, dass keine 2 Experimente mit dem gleichem Namen angelegt werden können
	*/
	@Test
	public void createExperiment()
	{
		try{
		String createPath = "http://localhost:4567/experiments/" + experimentName;
		FileInputStream fis;
		fis = new FileInputStream(new File("./src/test/resources/config.json"));
	    request = new PutMethodWebRequest(createPath, fis, "application/json");
	    WebResponse response = sc.getResponse( request );
		}catch(Exception e)
		{
			assertTrue(e.getMessage().contains("Error on HTTP request: 405 HTTP method PUT is not supported by this URL [http://localhost:4567/experiments/"+ experimentName));
		}
	}

	/*
	*Testet den CSV export
	*/
	@Test
	public void testRouteGetExperimentStatisticCSV() throws IOException, SAXException {			
		    WebRequest requestStatistic = new GetMethodWebRequest("http://localhost:4567/experiments/"+experimentName+"/csv");
		    WebResponse responseStatistic = sc.getResponse( requestStatistic );
		    assertEquals( "content type", "text/plain", responseStatistic.getContentType() );
		    assertTrue(responseStatistic.getText().contains("id"));
		    assertTrue(responseStatistic.getText().contains("timestamp"));
		    assertTrue(responseStatistic.getText().contains("text"));
			
		}
	/*
	*Testet den TXT export
	*/
	@Test
	public void testRouteGetExperimentStatisticTXT() throws IOException, SAXException {	
		
		    WebRequest requestStatistic = new GetMethodWebRequest("http://localhost:4567/experiments/"+experimentName+"/txt");
		    WebResponse responseStatistic = sc.getResponse( requestStatistic );
		    assertEquals( "content type", "text/plain", responseStatistic.getContentType() );
		    assertTrue(responseStatistic.getText().contains("TXT Export of all the answers"));
		}
	
	/*
	*Testet den Statistik export
	*/
	@Test
	public void testRouteGetExperimentStatistic() throws IOException, SAXException {		
		    WebRequest requestStatistic = new GetMethodWebRequest("http://localhost:4567/experiments/"+experimentName);
		    WebResponse responseStatistic = sc.getResponse( requestStatistic );
		    assertNotNull( "No response received", responseStatistic );
		    assertEquals( "content type", "text/plain", responseStatistic.getContentType() );
		    assertTrue(responseStatistic.getText().contains( experimentName));
		    assertTrue(responseStatistic.getText().contains("accepted"));
		    assertTrue(responseStatistic.getText().contains("submitted"));
		    assertTrue(responseStatistic.getText().contains("previewed"));
	}
	
	/*
	*Testet die Route New Show
	*/
	@Test
	public void testRouteNewShow() throws IOException, SAXException {	
		
		    WebRequest request = new GetMethodWebRequest("http://localhost:4567/assignment/"+experimentName + "?MY_ASSIGNMENT_ID");
		    WebResponse response = sc.getResponse( request );
		    assertEquals( "content type", "text/plain", response.getContentType() );	
	}
	
	
	/*
	 * Testet die Implementierung der Route End
	 */
	@Test
	public void testRouteEnd() throws IOException, SAXException {	
		    WebRequest request = new PostMethodWebRequest("http://localhost:4567/experiments/"+experimentName + "/end");;
		    WebResponse response = sc.getResponse( request );
		    assertEquals( "content type", "text/plain", response.getContentType() );	
		    assertTrue(response.getText().contains("successfully ended experiment with id"));
		    assertTrue(response.getText().contains(experimentName));
	}
	
	
	
}
