package edu.kit.ipd.creativecrowd.operations;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import edu.kit.ipd.creativecrowd.operations.StrategyNotFoundException;
import edu.kit.ipd.creativecrowd.operations.Transaction;
/**
 * Die Testfälle dieser Klasse führen jeweiligen Strategielade-Methoden, die die jeweiligen
 * Transaktionen benutzen, aus.
 * Sie überprüfen ob die zurückgegebene Klasse Instanz der jeweiligen Strategie ist und stellen sicher,
 * dass keine Exception geworfen wird.
 *
 */
public class TransactionsTest {

	@Test
	public void testLoadTACCStrategy() {
		class LolTransaction extends Transaction {
			void doTest() {
				TotalAssignmentCountController s = null;
				Map<String, String> params = new HashMap<String, String>();
				params.put("tacc_class", "edu.kit.ipd.creativecrowd.operations.strategies.NAssignments");
				try {
					s = this.loadTACCStrategy(params);
				} catch (StrategyNotFoundException e) {
					fail("ging nicht: "+e.getMessage());
				}
				if(s == null || !(s instanceof TotalAssignmentCountController)) {
					fail("didn't load correct class");
				}
				
			}
		}
		LolTransaction t = new LolTransaction();
		t.doTest();		
		
	}
	
	@Test
	public void testLoadTCMStrategy() {
		class LolTransaction extends Transaction {
			void doTest() {
				TaskConstellationMutator s = null;
				Map<String, String> params = new HashMap<String, String>();
				params.put("tcm_class", "edu.kit.ipd.creativecrowd.operations.strategies.FreeformTaskConstellationMutator");
				try {
					s = this.loadTCMStrategy(params);
				} catch (StrategyNotFoundException e) {
					fail("ging nicht: "+e.getMessage());
				}
				if(s == null || !(s instanceof TaskConstellationMutator)) {
					fail("didn't load correct class");
				}
				
			}
		}
		LolTransaction t = new LolTransaction();
		t.doTest();		
		
	}
	
	@Test
	public void testLoadAQICStrategy() {
		class LolTransaction extends Transaction {
			void doTest() {
				AnswerQualityIndexCalculator s = null;
				Map<String, String> params = new HashMap<String, String>();
				params.put("aqic_class", "edu.kit.ipd.creativecrowd.operations.strategies.AverageRating");
				try {
					s = this.loadAQICStrategy(params);
				} catch (StrategyNotFoundException e) {
					fail("ging nicht: "+e.getMessage());
				}
				if(s == null || !(s instanceof AnswerQualityIndexCalculator)) {
					fail("didn't load correct class");
				}
				
			}
		}
		LolTransaction t = new LolTransaction();
		t.doTest();		
		
	}
	@Test
	public void testLoadRQICStrategy() {
		class LolTransaction extends Transaction {
			void doTest() {
				RatingQualityIndexCalculator s = null;
				Map<String, String> params = new HashMap<String, String>();
				params.put("rqic_class", "edu.kit.ipd.creativecrowd.operations.strategies.EnsureRatingDiversity");
				try {
					s = this.loadRQICStrategy(params);
				} catch (StrategyNotFoundException e) {
					fail("ging nicht: "+e.getMessage());
				}
				if(s == null || !(s instanceof RatingQualityIndexCalculator)) {
					fail("didn't load correct class");
				}
				
			}
		}
		LolTransaction t = new LolTransaction();
		t.doTest();		
		
	}
	@Test
	public void testLoadAPOCStrategy() {
		class LolTransaction extends Transaction {
			void doTest() {
				AssignmentPaymentOutcomeCalculator s = null;
				Map<String, String> params = new HashMap<String, String>();
				params.put("apoc_class", "edu.kit.ipd.creativecrowd.operations.strategies.DefaultAPOC");
				try {
					s = this.loadAPOCStrategy(params);
				} catch (StrategyNotFoundException e) {
					fail("ging nicht: "+e.getMessage());
				}
				if(s == null || !(s instanceof AssignmentPaymentOutcomeCalculator)) {
					fail("didn't load correct class");
				}
				
			}
		}
		LolTransaction t = new LolTransaction();
		t.doTest();		
		
	}
	@Test
	public void testLoadRSDStrategy() {
		class LolTransaction extends Transaction {
			void doTest() {
				RatingSufficiencyDecider s = null;
				Map<String, String> params = new HashMap<String, String>();
				params.put("rsd_class", "edu.kit.ipd.creativecrowd.operations.strategies.ConsentRatingDecider");
				try {
					s = this.loadRSDStrategy(params);
				} catch (StrategyNotFoundException e) {
					fail("ging nicht: "+e.getMessage());
				}
				if(s == null || !(s instanceof RatingSufficiencyDecider)) {
					fail("didn't load correct class");
				}
				
			}
		}
		LolTransaction t = new LolTransaction();
		t.doTest();		
		
	}
	@Test
	public void testLoadFTGtrategy() {
		class LolTransaction extends Transaction {
			void doTest() {
				FeedbackTextGenerator s = null;
				Map<String, String> params = new HashMap<String, String>();
				params.put("ftg_class", "edu.kit.ipd.creativecrowd.operations.strategies.FeedbackWithoutRatingText");
				try {
					s = this.loadFTGStrategy(params);
				} catch (StrategyNotFoundException e) {
					fail("ging nicht: "+e.getMessage());
				}
				if(s == null || !(s instanceof FeedbackTextGenerator)) {
					fail("didn't load correct class");
				}
				
			}
		}
		LolTransaction t = new LolTransaction();
		t.doTest();		
		
	}

}
