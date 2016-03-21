package edu.kit.ipd.creativecrowd.view;
import static org.junit.Assert.*;
import mockit.Mock;
import mockit.MockUp;

import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.creativecrowd.crowdplatform.AssignmentId;
import edu.kit.ipd.creativecrowd.crowdplatform.PlatformIdentity;
import edu.kit.ipd.creativecrowd.crowdplatform.WorkerId;
import edu.kit.ipd.creativecrowd.mutablemodel.ExperimentRepo;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCreativeTask;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.operations.MockExperiment;
import edu.kit.ipd.creativecrowd.readablemodel.CreativeTask;


public class ViewFacadeUnitTest {
	MutableExperiment ex;
	ExperimentRepo repo;
	CreativeTask cr;
	MockExperiment mexp;

	@Before
	public void setUp() {
		try {
			if(mexp == null){
				mexp = new MockExperiment(); 
			}
			ex = mexp.getExperiment();
			
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
	}
	
	@Test
	public void testShowStat(){
		ViewFacade view = new ViewFacade();
		String v = view.showStatus(ex);
		String hope = "";
		try{
		hope = 
				 "Creative task: Do a backflip\n"
				+ "Tags for this experiment: [tag, chimpalot]\n";
		}catch(Exception x){
			fail(x.getMessage());
		}
		assertTrue(v + " sollte " +hope+ "enthalten!", v.contains(hope));
		
	}
	@Test
	public void showTaskConstellationTest() throws Exception {
		ViewFacade view = new ViewFacade();
		MockUp<CreativeTask> cr = new MockUp<CreativeTask>(){
			@Mock
			public String getPictureLicenseURL(){
				return null;
			}
		};
		ex = (MutableExperiment) new MockExperiment().getExperiment();
		MutableCreativeTask c = ex.addCreativeTask();
		c.setDescription("Do a backflip");
		c.setPicture("", "");

		MutableAssignment as = ex.addAssignment();
		AssignmentId ak = new AssignmentId(PlatformIdentity.MTurk.getPrefix() + "123");
		as.setWorker(new WorkerId(PlatformIdentity.MTurk.getPrefix() + "worker"));
		as.setAssignmentID(ak);
		String v = view.previewCreative(ex);
		String needle = "<iframe src=\"http://xkcd.com/1656/\" id=\"ifr\"></iframe>";
		assertTrue(v+" sollte "+needle+" enthalten!", v.contains(needle));

		cr.tearDown();

	}
	
	@Test
	public void showDialogTest(){
		ViewFacade view = new ViewFacade();	
		String v = view.showDialog("hallo");
		assertTrue("asdf", v.contains("hallo"));
	}
	@Test
	public void showExportTest(){
		ViewFacade view = new ViewFacade();	
		String v = view.showExport(ex, "csv");
		assertTrue("CSV funktioniert nicht", v.contains("id, text, timestamp, qualityindex"));
		v = view.showExport(ex, "txt");
		assertTrue("TXT funktioniert nicht", v.contains("TXT Export of all the answers"));
	}
	
	
	
	
}
