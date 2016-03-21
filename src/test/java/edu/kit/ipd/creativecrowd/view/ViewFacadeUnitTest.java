package edu.kit.ipd.creativecrowd.view;
import static org.junit.Assert.*;
import mockit.Mock;
import mockit.MockUp;

import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.creativecrowd.mturk.AssignmentId;
import edu.kit.ipd.creativecrowd.mutablemodel.ExperimentRepo;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCreativeTask;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.view.MockExperiment;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.CreativeTask;
import edu.kit.ipd.creativecrowd.readablemodel.Experiment;


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
		hope = "ID: Test\n"
				+ "Creative task: Do a backflip\n"
				+ "Tags for this experiment: []\n";
		}catch(Exception x){
			fail(x.getMessage());
		}
		assertTrue(v + " sollte " +hope+ "enthalten!", v.contains(hope));
		
	}
	@Test
	public void showTaskConstellationTest(){
		ViewFacade view = new ViewFacade();
		MockUp<CreativeTask> cr = new MockUp<CreativeTask>(){
			@Mock
			public String getPictureLicenseURL(){
				return null;
			}
		};
		
		try {
			ex = (MutableExperiment) new MockExperiment().getExperiment();
			MutableCreativeTask c = ex.addCreativeTask();
			c.setDescription("Do a backflip");
			c.setPicture("", "");
		} catch (DatabaseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			MutableAssignment as = ex.addAssignment();
			AssignmentId ak = new AssignmentId(as.getID());
			as.setAssignmentID(ak);
			String v = view.showTaskConstellation(ex, null);
			System.out.println(v);
			String needle = "<button type=\"submit\" value=\"Submit\" id=\"subsub\" class=\"but\" name=\"button\">Submit</button>";
			assertTrue(v+" sollte "+needle+" enthalten!", v.contains(needle));
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
