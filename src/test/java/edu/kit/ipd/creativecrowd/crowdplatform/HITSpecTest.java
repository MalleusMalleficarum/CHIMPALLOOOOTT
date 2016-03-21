package edu.kit.ipd.creativecrowd.crowdplatform;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;



public class HITSpecTest {
	HITSpec spec;
	String title = "Title";
	String description = "Description";
	String externalQuestionURL = "https://studium.kit.edu/_layouts/login.aspx?ReturnUrl=%2fsites%2fvab%2f0x1A42A9A52279304DA6CF18604A601D4C%2fvorlesungsunterlagen_pwg%2fForms%2fAllItems.aspx";
	int frameHeight = 800;
	int rewardCents = 20;
	int numAssignments = 40;
	int completionTime = 5000;
	int estimate = 90;
	int expireTime = 80;
	
	
	
	@Before
	public void workingHITSpec() throws IllegalInputException
	{
		spec = new HITSpec(title, description, rewardCents, numAssignments, externalQuestionURL , estimate, completionTime, null);
	}
	
	/*
	 * Testet wenn der Title auf Null gesetzt wird; erwartet eine Exception
	 */
	@Test(expected=IllegalInputException.class)
	public void titleIsNull() throws IllegalInputException
	{
		spec.setTitle(null);
	}
	
	/*
	 * Testet ob der title richtig gesetzt wurde.
	 */
	@Test
	public void correctTitle() throws IllegalInputException
	{
		assertEquals(title, spec.getTitle());
	}
	
	/*
	 * Testet wenn man eine falsche URL als externalQuestion wählt
	 */
	@Test(expected=IllegalInputException.class)
	public void setExternalQuestionWrongURL()  throws IllegalInputException
	{		//no https
		spec.setExternalQuestion("http://studium.kit.edu/_layouts/login.aspx?ReturnUrl=%2fsites%2fvab%2f0x1A42A9A52279304DA6CF18604A601D4C%2fvorlesungsunterlagen_pwg%2fForms%2fAllItems.aspx", frameHeight);
	}
	
	/*
	 * Testet das Setzen eine externen URL die null ist
	 */
	@Test(expected=IllegalInputException.class)
	public void setExternalQuestionNoURL()  throws IllegalInputException
	{		
		spec.setExternalQuestion(null, frameHeight);
	}
	
	/*
	 * Testet das Setzen einer externen URL mit einer negativen Frame Höhe
	 */
	@Test(expected=IllegalInputException.class)
	public void setExternalQuestionNegativeFrameHeight()  throws IllegalInputException
	{		
		spec.setExternalQuestion("https://studium.kit.edu/_layouts/login.aspx?ReturnUrl=%2fsites%2fvab%2f0x1A42A9A52279304DA6CF18604A601D4C%2fvorlesungsunterlagen_pwg%2fForms%2fAllItems.aspx", -20);
	}
	
	/*
	 * Testet das Setzen von 0 als Frame Höhe bei dem setzen einer externen URL
	 */
	@Test(expected=IllegalInputException.class)
	public void setExternalQuestionZeroFrameHeight()  throws IllegalInputException
	{		
		spec.setExternalQuestion("https://studium.kit.edu/_layouts/login.aspx?ReturnUrl=%2fsites%2fvab%2f0x1A42A9A52279304DA6CF18604A601D4C%2fvorlesungsunterlagen_pwg%2fForms%2fAllItems.aspx", 0);
	}
	
	/*
	 * prüft ob eine korrekte externe question gesetzt werden kann
	 */
	@Test
	public void correctSetExternalQuestion()  throws IllegalInputException
	{
		spec.setExternalQuestion(externalQuestionURL, frameHeight);
		assertEquals(spec.getExternalQuestion(), "<?xml version=\"1.0\"?>\n<ExternalQuestion xmlns=\"http://mechanicalturk.amazonaws.com/AWSMechanicalTurkDataSchemas/2006-07-14/ExternalQuestion.xsd\">\n<ExternalURL>"
				+ externalQuestionURL
				+ "</ExternalURL>\n<FrameHeight>"
				+ frameHeight + "</FrameHeight>\n</ExternalQuestion>");
	}
	
	/*
	 * testet ob die Description richtig gesetzt wurde
	 */
	@Test
	public void correctDescription() throws IllegalInputException
	{
		assertEquals(spec.getDescription(), description);
	}
	
	/*
	 * testet ob die rewardCents richtig gesetzt wurden
	 */
	@Test
	public void correctRewardCents() throws IllegalInputException
	{
		assertEquals(spec.getRewardCents(), rewardCents);
	}
	
	/*
	 * Testet ob die rewardCents richtig gesetzt werden
	 */
	@Test
	public void correctSetRewardCents() throws IllegalInputException
	{
		int newReward = 5000;
		spec.setRewardCents(newReward);
		assertEquals(spec.getRewardCents(), newReward);
	}
	
	/*
	 * testet ob ein Fehler geworfen wird wenn die RewardCents negativ werden
	 */
	@Test(expected=IllegalInputException.class)
	public void negativeRewardCents() throws IllegalInputException
	{
		spec.setRewardCents(-1);
	}
	
	/*
	 * Prüft ob die Keys auf null gesetzt werden können
	 */
	@Test
	public void setKeywordsNull()
	{
		spec.setKeywords(null);
		assertEquals(spec.getKeywords(), "");
	}
	
	/*
	 * Prüft ob die Keys korrekt gesetzt werden
	 */
	@Test
	public void setKeywords()
	{
		String [] keywords = {"test", "keyword", "drei"};
		spec.setKeywords(keywords);
		assertEquals(spec.getKeywords(), "test, keyword, drei");
	}
	
	
	/*
	 * testet ob die completiontime correct gesetzt wurde
	 */
	@Test 
	public void completiontimeCorrect()
	{
		assertEquals(spec.getCompletiontime(), completionTime);
	}
	
	/*
	 * testet das Setzen einer zu kleinen completiontime
	 */
	 @Test(expected=IllegalInputException.class)
	 public void completionTimeLess() throws IllegalInputException
	 {
		 spec.setCompletiontime(29);
	 }
	
	 @Test(expected=IllegalInputException.class)
	 public void completionTimeHigh() throws IllegalInputException
	 {
		 spec.setCompletiontime(31536002);
	 }
	 
	 /*
	  * testet das Setzen einer korrekten completiontime
	  */
	 @Test
	 public void setCompletionTime() throws IllegalInputException
	 {	int newTime = 89;
		 spec.setCompletiontime(newTime);
	 assertEquals(spec.getCompletiontime(),newTime);
	 }
	 
	 
	 /*
	  * testet das Setzen einer korrekten expireTime
	  */
		@Test 
		public void expireCorrect() throws IllegalInputException
		{
			spec.setExpireingtime(expireTime);
			assertEquals(spec.getExpireingtime(), expireTime);
		}
		
		/*
		 * testet das Setzen einer zu kleinen expireTime
		 */
		 @Test(expected=IllegalInputException.class)
		 public void expireTimeLess() throws IllegalInputException
		 {
			 spec.setExpireingtime(29);
		 }
		
		 /*
		  * testet das das Setzen einer zu großen expiretime
		  */
		 @Test(expected=IllegalInputException.class)
		 public void expireTimeHigh() throws IllegalInputException
		 {
			 spec.setExpireingtime(31536002);
		 }
		 
		 /*
		  * testet das Setzen einer  richtigen expireTime
		  */
		 @Test
		 public void setExpireTime() throws IllegalInputException
		 {	int newTime = 89;
			 spec.setExpireingtime(newTime);
		 assertEquals(spec.getExpireingtime(),newTime);
		 }
	 
		 /*
		  * testet ob die Anzahl der Assignments richtig gesetzt wurde
		  */
		 @Test
		 public void numAssignments()
		 {
			 assertEquals(spec.getNumAssignments(), numAssignments);
			 
		 }
	 
		 /*
		  * testet ob die Anzahl der Assignments richtig gesetzt werden
		  */
		 @Test
		 public void numAssignmentsCorrect() throws IllegalInputException
		 { int newNum = 20;
			 spec.setNumAssignments(newNum);
			 assertEquals(spec.getNumAssignments(), newNum);
		 }
	 
		 /*
		  * testet die Eingabe von 0 Assignments
		  */
		 @Test(expected=IllegalInputException.class)
		 public void numAssignmentsLess() throws IllegalInputException
		 { int newNum = 0;
			 spec.setNumAssignments(newNum);
			 assertEquals(spec.getNumAssignments(), newNum);
		 } 
		 
		 /*
		  * testet wenn zu viele Assignments angegeben werden
		  */
		 @Test(expected=IllegalInputException.class)
		 public void numAssignmentsHigh() throws IllegalInputException
		 { int newNum = 1000000001;
			 spec.setNumAssignments(newNum);
			 assertEquals(spec.getNumAssignments(), newNum);
		 }
	 
		 
		 /*
		  * testet das setzen von Qualifications als null
		  */
		 @Test
		 public void setQualificationsNull() throws IllegalInputException
		 {
			 spec.setQualifications(null);
			 assertArrayEquals(spec.getQualifications(), null);
		 }
		 
		 @Test
		 public void setAndGetExternalURL() {
			 spec.setExternalURL("I bin oa URL");
			 assertEquals("I bin oa URL", spec.getExternalURL());
			 
		 }
		 
		 
		 
	
	@After
	public void deleteHITSpec()
	{
		spec = null;
	}
}
