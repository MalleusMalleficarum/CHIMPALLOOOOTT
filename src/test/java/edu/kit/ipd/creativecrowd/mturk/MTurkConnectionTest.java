package edu.kit.ipd.creativecrowd.mturk;
import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
//import mockit.*;





import edu.kit.ipd.creativecrowd.mturk.ConnectionFailedException;
import edu.kit.ipd.creativecrowd.mturk.HITSpec;
import edu.kit.ipd.creativecrowd.mturk.AssignmentId;
import edu.kit.ipd.creativecrowd.mturk.MTurkConnection;
import edu.kit.ipd.creativecrowd.util.GlobalApplicationConfig;


public class MTurkConnectionTest {
	MTurkConnection turkConnection ;
	String HITId;
	int numAssignments = 300;
	/**
	 * Creating a new HIT on MTurk for test purposes
	 * @throws ConnectionFailedException
	 * @throws IllegalInputException
	 */
	@Before
	public void createHit() throws ConnectionFailedException, IllegalInputException
	{	String keywords [] = new String[3];
		keywords[0] = "keyword";
		keywords[1] = "testen";
		keywords[2] = "ist_toll";
		GlobalApplicationConfig.configureFromServletContext(null);
			turkConnection = new MTurkConnection();
			Calendar cal = GregorianCalendar.getInstance();
			HITSpec spec = new HITSpec(cal.getTime().toString(), "Description1", 50, numAssignments, "https://studium.kit.edu/_layouts/login.aspx?ReturnUrl=%2fsites%2fvab%2f0x1A42A9A52279304DA6CF18604A601D4C%2fvorlesungsunterlagen_pwg%2fForms%2fAllItems.aspx", 31536, 111111, null);
			
			spec.setKeywords(keywords);
			HITId = turkConnection.publishHIT(spec);

	}
	
	
	
	/**
	 * Deletes the created hit on MTurk to clean up
	 * @throws IllegalInputException
	 */
	@After
	public void deleteHit() throws IllegalInputException
	{
		turkConnection.endHIT(HITId);
	}
	
	/*
	 * Kontrolliert die Funktion checkBudget
	 */
	@Test
	public void checkAccountBalanceExactMoney() throws ConnectionFailedException, IllegalInputException
	{
		assertTrue(turkConnection.checkBudget(1000000));
	}
	
	/*
	 * Kontrolliert einen falschen Geldbetrag
	 */
	@Test
	public void checkAccountBalanceToMuchMoney() throws ConnectionFailedException, IllegalInputException
	{
		assertFalse(turkConnection.checkBudget(1000001));
	}
	
	@Test(expected=IllegalInputException.class)
	public void checkAccountBalanceLessThanZero() throws ConnectionFailedException, IllegalInputException
	{
		turkConnection.checkBudget(-1);
	}
	
	/*
	 * Prüft ob das anlegen eines hits funktioniert
	 */
	@Test 
	public void HITExcists()
	{
		assertNotNull(HITId);
	}
	
	/*
	 * kontrolliert ob die RequesterDaten stimmen/die Funktion funktioniert
	 */
	@Test
	public void validateRequesterData() throws ConnectionFailedException
	{
		assertTrue(turkConnection.validateRequesterData());
	}
	
	/*
	 * funktioniert das approven eines HITs bei einer falschen ID
	 */
	public void approveHITInvalidID() throws IllegalInputException
	{
		turkConnection.approveHIT(new AssignmentId("new_assignment_id"), "my feedback message");
	}
	
	/*
	 * Prüft das Senden eines Bonuses an eine falsche ID
	 */
	public void sendBonusInvaldID() throws IllegalInputException
	{
		turkConnection.sendBonus(new AssignmentId("new_assignment_id"), new WorkerId("new_Worker"), 200, "my feedback message");
	}
	
	/*
	 * Prüft das Senden eines negativen Bonus
	 */
	@Test(expected=IllegalInputException.class)
	public void sendBonusNegativeBonus() throws IllegalInputException
	{
		turkConnection.sendBonus(new AssignmentId("new_assignment_id"), new WorkerId("new_Worker"), -200, "my feedback message");
	}	
	
	/*
	 * Prüft das Senden eines Bonus der zu groß ist
	 */
	@Test(expected=IllegalInputException.class)
	public void sendBonusToMuchMoney() throws IllegalInputException
	{
		turkConnection.sendBonus(new AssignmentId("new_assignment_id"), new WorkerId("new_Worker"), 51536000, "my feedback message");
	}	
	
	/*
	 * Testet das Abweisen eines Assignments mit keinem Feedback
	 */
	@Test(expected=IllegalInputException.class)
	public void rejectAssignmentNoReason() throws IllegalInputException
	{
		turkConnection.rejectAssignment(new AssignmentId("new_assignment_id"), null);
	}
	
	/*
	 * Testet das Abweisen eines Assignments mit einer falschen AssignmentId
	 */
	@Test(expected=IllegalInputException.class)
	public void rejectAssignmentWrongAssignmentId() throws IllegalInputException
	{
		turkConnection.rejectAssignment(new AssignmentId("new_assignment_id"), "you are a spoofer");
	}
	
	/*
	 * prüft ob die availableHits richtig gesetzt wurden
	 */
	@Test
	public void availableHits() throws IllegalInputException
	{
		assertEquals(turkConnection.getAvailableAssignments(HITId), numAssignments);
	}
	
	/*
	 * prüft das Abfragen der verfügbaren HITS einer falschen HIT Id
	 */
	@Test(expected=IllegalInputException.class)
	public void availableHitsWrongId() throws IllegalInputException
	{
		assertEquals(turkConnection.getAvailableAssignments("Wrong_HIT_ID"), numAssignments);
	}
	
	/*
	 * Testet das Erweitern eines Assignments
	 */
	@Test
	public void extendAssignmentNumber() throws IllegalInputException
	{
		int currentAssignments = turkConnection.getAvailableAssignments(HITId);
		turkConnection.extendAssignmentNumber(numAssignments + 25, HITId);
		assertEquals(turkConnection.getAvailableAssignments(HITId),numAssignments + 25);
		
	}
	
	/*
	 * Testet das Erweitern eines Assignments mit einer falschen HIT ID
	 */
	@Test(expected=IllegalInputException.class)
	public void extendAssignmentWrongHITId() throws IllegalInputException
	{
		turkConnection.extendAssignmentNumber(25, "Wrong_HIT_ID");
	}
	
	/*
	 * testet das Erweitern eines Assignments mit einer negativen Anzahl an Erweiterungen
	 */
	@Test(expected=IllegalInputException.class)
	public void extendAssignmentNegativeExtension() throws IllegalInputException
	{
		turkConnection.extendAssignmentNumber(numAssignments - 5, HITId);
	}
	
	
	
	
}
