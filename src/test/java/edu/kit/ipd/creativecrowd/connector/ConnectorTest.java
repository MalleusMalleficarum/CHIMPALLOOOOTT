package edu.kit.ipd.creativecrowd.connector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.creativecrowd.readablemodel.Task;
import edu.kit.ipd.creativecrowd.mturk.AssignmentId;
import edu.kit.ipd.creativecrowd.mturk.ConnectionFailedException;
import edu.kit.ipd.creativecrowd.mturk.IllegalInputException;
import edu.kit.ipd.creativecrowd.mutablemodel.ExperimentRepo;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCreativeTask;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRating;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingOption;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableStats;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableTaskConstellation;
import edu.kit.ipd.creativecrowd.operations.CreateExperimentTransaction;
import edu.kit.ipd.creativecrowd.operations.EndExperimentTransaction;
import edu.kit.ipd.creativecrowd.operations.NoValidTasksException;
import edu.kit.ipd.creativecrowd.operations.StrategyNotFoundException;
import edu.kit.ipd.creativecrowd.operations.SubmitAssignmentTransaction;
import edu.kit.ipd.creativecrowd.operations.UpdateTaskConstellationTransaction;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.persistentmodel.PersistentExperimentRepo;
import edu.kit.ipd.chimpalot.util.GlobalApplicationConfig;
import edu.kit.ipd.chimpalot.util.Logger;
import edu.kit.ipd.creativecrowd.controller.ExperimentSpecFromConfig;
import edu.kit.ipd.creativecrowd.readablemodel.Assignment;
import edu.kit.ipd.creativecrowd.readablemodel.Button;
import edu.kit.ipd.creativecrowd.readablemodel.ExperimentSpec;
import mockit.Mock;
import mockit.MockUp;

/**
 * @author Tobias
 *
 */
public class ConnectorTest {
	Connector connector;
	MockUp mocki [] = new MockUp[5];
//	@Mocked
//	CreateExperimentTransaction mCreateExperimentTransaction;

	@Before
	public void setUp()
	{
		GlobalApplicationConfig.configureFromServletContext(null);
		connector = new Connector();
	}
	
	@After
	public void tearDown()
	{
		connector = null;
		for(MockUp mock: mocki)
		{
			if(mock!=null){
				try{
			mock.tearDown();
				try{
					mock.tearDown();
				}catch(NullPointerException e)
				{
					
				}
				}catch(Exception e)
				{
					System.out.println("Scheiß java trolling");
				}
			}
		}
	}
	
	

	
	
	/**
	 * Prüft, ob der Connector alle Methodenaufrufe tätigt und bei einer funktionierenden  Modellschicht funktioniert.
	 */
	@Test
	public void createExperimentFromSpecsWorking() throws DatabaseException, StrategyNotFoundException, ConnectionFailedException, IllegalInputException, ModelException
	{

		
		mocki[0] = new MockUp<CreateExperimentTransaction>(){
			@Mock
			MutableExperiment run(ExperimentRepo repo, ExperimentSpec spec){
				return null;
			}
		};
		Map<String, Double> map = new HashMap<String, Double>();
		ExperimentSpecFromConfig spec = new ExperimentSpecFromConfig(0, 0, 0, 0, 0, null, null,null, null, null, null, null, map, null, null, null, null, null, 0, 0);
		connector.createExperimentFromSpecs(spec);
	}
	/**
	 * Prüft, ob der Connector alle Methodenaufrufe bei dem erstellen eines Experiments tätigt und bei einer nicht funktionierenden  Modellschicht funktioniert.
	 */
	@Test(expected=ModelException.class)
	public void createExperimentFromSpecsException() throws DatabaseException, StrategyNotFoundException, ConnectionFailedException, IllegalInputException, ModelException
	{
		GlobalApplicationConfig.configureFromServletContext(null);
		
		mocki[0] = new MockUp<CreateExperimentTransaction>(){
			@Mock
			MutableExperiment run(ExperimentRepo repo, ExperimentSpec spec) throws DatabaseException{
				throw new DatabaseException("DatabaseError");
			}
		};
		Map<String, Double> map = new HashMap<String, Double>();
		ExperimentSpecFromConfig spec = new ExperimentSpecFromConfig(0, 0, 0, 0, 0, null,null, null, null, null, null, null, map, null, null, null, null, null, 0, 0);
		connector.createExperimentFromSpecs(spec);
	}
	
	/**
	 * Testet ob bei der Methode getExperiment alle nötigen Methodenaufrufe getätigt werden und unter der vorraussetzung einer funktionierenden Modellschicht funktioniert.
	*/
	@Test
	public void getExperimentWorking() throws ModelException
	{
		GlobalApplicationConfig.configureFromServletContext(null);
		
		mocki[0] = new MockUp<PersistentExperimentRepo>(){
			@Mock
			MutableExperiment loadExperiment(String name) throws DatabaseException{
				if(name=="Test")
				return null;
				throw new DatabaseException("Wrong Testname");
			}
			
		};
		connector.getExperiment("Test");
		assertEquals(connector.getExperiment("Test"), null);
		
		
	}
	/**
	 * Testet ob bei der Methode getExperimentWorking alle nötigen Methodenaufrufe getätigt werden und unter der vorraussetzung einer falschen Eingabe funktioniert.
	*/
	@Test(expected=ModelException.class)
	public void getExperimentException() throws ModelException
	{
		GlobalApplicationConfig.configureFromServletContext(null);
		
		mocki[0] = new MockUp<PersistentExperimentRepo>(){
			@Mock
			MutableExperiment loadExperiment(String name) throws DatabaseException{
				if(name=="Test")
				return null;
				throw new DatabaseException("Wrong Testname");
			}
			
		};
		connector.getExperiment("Test_NOT_Working");
		assertEquals(connector.getExperiment("Test"), null);
		
		
	}
	
	/**
	 * Testet ob bei der Methode getNewTaskConstellation alle nötigen Methodenaufrufe getätigt werden und unter der vorraussetzung einer funktionierenden Modellschicht funktioniert.
	*/
	@Test
	public void getNewTaskConstellationWorking() throws ModelException
	{			
		GlobalApplicationConfig.configureFromServletContext(null);
		
		mocki[0] = new MockUp<PersistentExperimentRepo>(){
			@Mock
			MutableExperiment loadExperiment(String name) throws DatabaseException{
				if(name=="Test"){
				return (MutableExperiment) new MockExperiment();
				}
				throw new DatabaseException("Wrong Testname");
			}
			
		};
		mocki[1] = 	new MockUp<MockExperiment>(){
			@Mock
			MutableAssignment getAssignment(String assgId){
				if(assgId=="42")
				{
				return (MutableAssignment) new MockAssignment();
				}
				return null;
			}	
		};
		mocki[2] = new MockUp<UpdateTaskConstellationTransaction>(){
			@Mock
			public MutableTaskConstellation run(MutableExperiment experiment,MutableAssignment as, Button button ){
				if(experiment!=null&&as!=null&&button!=null)
				{
					return new MockTaskConstellation();
				}
				return null;
			}
		};
		MutableTaskConstellation task = (MutableTaskConstellation) connector.getNewTaskConstellation("Test", "42", Button.Start);
		assertNotNull(task);
	}
	
	
	
	
	
	/**
	 * Testet ob bei der Methode getNewTaskConstellation alle nötigen Methodenaufrufe getätigt werden und unter der vorraussetzung einer falschen Eingabe funktioniert.
	*/
	@Test
	public void getNewTaskConstellationDatabaseException() throws ModelException
	{			
		GlobalApplicationConfig.configureFromServletContext(null);
		
		mocki[0] = new MockUp<PersistentExperimentRepo>(){
			@Mock
			MutableExperiment loadExperiment(String name) throws DatabaseException{
				if(name=="Test"){
				return (MutableExperiment) new MockExperiment();
				}
				throw new DatabaseException("Wrong Testname");
			}
			
		};
		mocki[1] = new MockUp<MockExperiment>(){
			@Mock
			MutableAssignment getAssignment(String assgId){
				if(assgId=="42")
				{
				return (MutableAssignment) new MockAssignment();
				}
				return null;
			}	
			@Mock
			MutableAssignment addAssignment(){

				return (MutableAssignment) new MockAssignment();

			}	
		};
		mocki[2] = new MockUp<UpdateTaskConstellationTransaction>(){
			@Mock
			public MutableTaskConstellation run(MutableExperiment experiment,MutableAssignment as, Button button ){
				if(experiment!=null&&as==null&&button!=null)
				{
					return new MockTaskConstellation();
				}
				return null;
			}
		};
		MutableTaskConstellation task = (MutableTaskConstellation) connector.getNewTaskConstellation("Test", "422", Button.Start);
		assertNotNull(task);
	}
	
	
	/**
	 * Testet ob bei der Methode getNewTaskConstellation alle nötigen Methodenaufrufe getätigt werden und unter der vorraussetzung einer falschen Eingabe funktioniert.
	*/
	@Test(expected=ModelException.class)
	public void getNewTaskConstellationModelException() throws ModelException
	{			
		GlobalApplicationConfig.configureFromServletContext(null);
		
		mocki[0] = new MockUp<PersistentExperimentRepo>(){
			@Mock
			MutableExperiment loadExperiment(String name) throws DatabaseException{
				if(name=="Test"){
				return (MutableExperiment) new MockExperiment();
				}
				throw new DatabaseException("Wrong Testname");
			}
			
		};
		mocki[1] = new MockUp<MockExperiment>(){
			@Mock
			MutableAssignment getAssignment(String assgId) throws NoValidTasksException{
				throw new NoValidTasksException();
			}	
		};
		mocki[2] = new MockUp<UpdateTaskConstellationTransaction>(){
			@Mock
			public MutableTaskConstellation run(MutableExperiment experiment,MutableAssignment as, Button button ){
				if(experiment!=null&&as!=null&&button!=null)
				{
					return new MockTaskConstellation();
				}
				return null;
			}
		};
		MutableTaskConstellation task = (MutableTaskConstellation) connector.getNewTaskConstellation("Test", "42", Button.Start);
		assertNotNull(task);
	}
	
	
	
	
	
	
	/**
	 * Testet ob bei der Methode store alle nötigen Methodenaufrufe getätigt werden und unter der vorraussetzung einer funktionierenden Modellschicht funktioniert.
	*/
	@Test
	public void storeTestWorking() throws ModelException
	{
		GlobalApplicationConfig.configureFromServletContext(null);
		
		mocki[0] = new MockUp<PersistentExperimentRepo>(){
			@Mock
			MutableExperiment loadExperiment(String name){
				if(name == "Test")
				{
					return (MutableExperiment) new MockExperiment();
				}
				return null;
			}
		};
		
		mocki[1] = new MockUp<MockExperiment>(){
			@Mock
			MutableAssignment getAssignment(String assignmentid)
			{
				if(assignmentid == "Test2")
				{
					return new MockAssignment();
				}
				return null;
			}
		};
		mocki[2] = new MockUp<MockAssignment>(){
			@Mock
			MutableTaskConstellation getTaskConstellation()
			{
				return (MutableTaskConstellation) new MockTaskConstellation();
			}
		};
		
		mocki[3] = new MockUp<MockTaskConstellation>(){
			@Mock
			int getCurrentTaskIndex()
			{
				return 42;
			}
			@Mock
			MutableAnswer answerCreativeTaskAt(int index)
			{
				if(index==42)
				{
					return new MockAnswer();
				}
				return null;
			}
		};
		
		mocki[4] = new MockUp<MockAnswer>(){
			@Mock
			void setText(String answer)
			{
				
			}
		};
		MockAnswerSpec testSpec = new MockAnswerSpec();
		connector.store("Test", testSpec, "Test2");
		
	}
	
	/**
	 * Testet ob bei der Methode store alle nötigen Methodenaufrufe getätigt werden und unter der vorraussetzung einer nicht funktionierenden Modellschicht funktioniert.
	*/
	@Test(expected=ModelException.class)
	public void storeTestFail() throws ModelException
	{
		GlobalApplicationConfig.configureFromServletContext(null);
		
		mocki[0] = new MockUp<PersistentExperimentRepo>(){
			@Mock
			MutableExperiment loadExperiment(String name){
				if(name == "Test")
				{
					return (MutableExperiment) new MockExperiment();
				}
				return null;
			}
		};
		
		mocki[1] = new MockUp<MockExperiment>(){
			@Mock
			MutableAssignment getAssignment(String assignmentid) throws DatabaseException
			{
				if(assignmentid == "Test2")
				{
					//return new MockAssignment();
					throw new DatabaseException("Datafail");
				}
				return null;
			}
		};
		mocki[2] = new MockUp<MockAssignment>(){
			@Mock
			MutableTaskConstellation getTaskConstellation()
			{
				return (MutableTaskConstellation) new MockTaskConstellation();
			}
		};
		
		mocki[3] = new MockUp<MockTaskConstellation>(){
			@Mock
			int getCurrentTaskIndex()
			{
				return 42;
			}
			@Mock
			MutableAnswer answerCreativeTaskAt(int index)
			{
				if(index==42)
				{
					return new MockAnswer();
				}
				return null;
			}
		};
		
		mocki[4] = new MockUp<MockAnswer>(){
			@Mock
			void setText(String answer)
			{
				
			}
		};
		MockAnswerSpec testSpec = new MockAnswerSpec();
		connector.store("Test", testSpec, "Test2");
		
	}
	
	
	/**
	 * Testet ob bei der Methode store alle nötigen Methodenaufrufe getätigt werden und unter der vorraussetzung einer funktionierenden Modellschicht funktioniert.
	*/
	@Test
	public void store_2Working() throws ModelException
	{
		GlobalApplicationConfig.configureFromServletContext(null);
		
		mocki[0] = new MockUp<PersistentExperimentRepo>(){
			@Mock
			MutableExperiment loadExperiment(String name){
				if(name == "Test")
				{
					return (MutableExperiment) new MockExperiment();
				}
				return null;
			}
			};
			
			mocki[1] = new MockUp<MockExperiment>(){
				@Mock
				public MutableCreativeTask getCreativeTask()
				{
					return (MutableCreativeTask) new MockCreativeTask();
				}
				@Mock
				MutableAssignment getAssignment(String assignmentid){
					if(assignmentid=="TestId")
					return (MutableAssignment) new MockAssignment();
					return null;
				}
				@Mock
				Iterable<MutableRatingOption> getRatingOptions(){
					//List<MutableRatingOption> ratingOptions = new ArrayList<MutableRatingOption>();
					Iterable<MutableRatingOption> ratingOptions = new ArrayList<MutableRatingOption>();
					//return (Iterable<MutableRatingOption>) ratingOptions;
					return ratingOptions;
				}
				
			};	
			
			mocki[2] = new MockUp<MockAssignment>(){
				@Mock
				public MutableTaskConstellation getTaskConstellation()
				{
					return new MockTaskConstellation();
				}
			};
			
			mocki[3] = new MockUp<MockTaskConstellation>(){
				@Mock
				public Task getCurrentTask()
				{
					return (Task) new MockRatingTask();
				}
				@Mock
				public int getCurrentTaskIndex()
				{
					return 42;
				}
				@Mock
				public MutableRating addRatingToRatingTaskAt(int index)
				{
					return (MutableRating) new MockRating();
				}
			};
			
			mocki[4] = new MockUp<MockRatingTask>(){
				@Mock
				public MutableAnswer getAnswerToBeRated(String id)
				{
					if(id == "Answer")
					{
						return (MutableAnswer) new MockAnswer();
					}
					return null;
				}
			};
			
			
		RatingSpec spec = new MockRatingSpec();
		connector.store("Test", spec, "TestId");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Testet ob bei der Methode store alle nötigen Methodenaufrufe getätigt werden und unter der vorraussetzung einer falschen Eingabe funktioniert.
	*/
	@Test(expected=ModelException.class)
	public void store_2Fail() throws ModelException
	{
		
			GlobalApplicationConfig.configureFromServletContext(null);
			
			mocki[0] = 	new MockUp<PersistentExperimentRepo>(){
				@Mock
				MutableExperiment loadExperiment(String name) throws DatabaseException{
					if(name == "Test")
					{
						return (MutableExperiment) new MockExperiment();
					}
					throw new DatabaseException("Database Put");
				}
				};
				
				mocki[1] = 	new MockUp<MockExperiment>(){
					@Mock
					public MutableCreativeTask getCreativeTask()
					{
						return (MutableCreativeTask) new MockCreativeTask();
					}
					@Mock
					MutableAssignment getAssignment(String assignmentid){
						if(assignmentid=="TestId")
						return (MutableAssignment) new MockAssignment();
						return null;
					}
					@Mock
					Iterable<MutableRatingOption> getRatingOptions(){
						//List<MutableRatingOption> ratingOptions = new ArrayList<MutableRatingOption>();
						Iterable<MutableRatingOption> ratingOptions = new ArrayList<MutableRatingOption>();
						//return (Iterable<MutableRatingOption>) ratingOptions;
						return ratingOptions;
					}
					
				};	
				
				mocki[2] = 	new MockUp<MockAssignment>(){
					@Mock
					public MutableTaskConstellation getTaskConstellation()
					{
						return new MockTaskConstellation();
					}
				};
				
				mocki[3] = new MockUp<MockTaskConstellation>(){
					@Mock
					public Task getCurrentTask()
					{
						return (Task) new MockRatingTask();
					}
					@Mock
					public int getCurrentTaskIndex()
					{
						return 42;
					}
					@Mock
					public MutableRating addRatingToRatingTaskAt(int index)
					{
						return (MutableRating) new MockRating();
					}
				};
				
				mocki[4] = 	new MockUp<MockRatingTask>(){
					@Mock
					public MutableAnswer getAnswerToBeRated(String id)
					{
						if(id == "Answer")
						{
							return (MutableAnswer) new MockAnswer();
						}
						return null;
					}
				};
				
				
			RatingSpec spec = new MockRatingSpec();
			connector.store("Test2", spec, "TestId");
		
	}
	
	/**
	 * Testet ob bei der Methode submit alle nötigen Methodenaufrufe getätigt werden und unter der vorraussetzung einer funktionierenden Modellschicht funktioniert.
	*/
	@Test
	public void submitWorking() throws ModelException
	{
		GlobalApplicationConfig.configureFromServletContext(null);
		
		mocki[0] = new MockUp<PersistentExperimentRepo>(){
			@Mock
			MutableExperiment loadExperiment(String name) throws DatabaseException{
				if(name == "Test")
				{
					return (MutableExperiment) new MockExperiment();
				}
				throw new DatabaseException("Database Put");
			}
			};
			
			mocki[1] = new MockUp<MockExperiment>(){
			@Mock
			MutableAssignment getAssignment(String assignmentid){
				if(assignmentid=="TestId")
				return (MutableAssignment) new MockAssignment();
				return null;
			}
			};
			
			
			mocki[2] = new MockUp<SubmitAssignmentTransaction>()
			{
				@Mock
				public void run(MutableAssignment ass, MutableExperiment experiment) throws IllegalInputException
				{
					if(ass!=null&&experiment!=null)
					{
						return;
					}
					throw new IllegalInputException("Illegal", "Input");
				}
			};
			connector.submit("Test", "TestId");
			
			
	}
	
	/**
	 * Testet ob bei der Methode submit alle nötigen Methodenaufrufe getätigt werden und unter der vorraussetzung einer nicht funktionierenden Modellschicht funktioniert.
	*/
	@Test(expected=ModelException.class)
	public void submitFail() throws ModelException
	{
		GlobalApplicationConfig.configureFromServletContext(null);
		
		mocki[0] = new MockUp<PersistentExperimentRepo>(){
			@Mock
			MutableExperiment loadExperiment(String name) throws DatabaseException{
				if(name == "Test")
				{
					return (MutableExperiment) new MockExperiment();
				}
				throw new DatabaseException("Database Put");
			}
			};
			
			mocki[1] = new MockUp<MockExperiment>(){
			@Mock
			MutableAssignment getAssignment(String assignmentid){
				if(assignmentid=="TestId")
				return (MutableAssignment) new MockAssignment();
				return null;
			}
			};
			
			
			mocki[2] = new MockUp<SubmitAssignmentTransaction>()
			{
				@Mock
				public void run(MutableAssignment ass, MutableExperiment experiment) throws IllegalInputException
				{
					if(ass!=null&&experiment!=null)
					{
						return;
					}
					throw new IllegalInputException("Illegal", "Input");
				}
			};
			connector.submit("Test", "Test124Id");
			
			
	}
	
	/**
	 * Testet ob bei der Methode endExperiment alle nötigen Methodenaufrufe getätigt werden und unter der vorraussetzung einer funktionierenden Modellschicht funktioniert.
	*/
	@Test
	public void endExperimentWorking() throws ModelException
	{
		GlobalApplicationConfig.configureFromServletContext(null);
		
		mocki[0] = new MockUp<PersistentExperimentRepo>(){
			@Mock
			MutableExperiment loadExperiment(String name) throws DatabaseException{
				if(name == "Test")
				{
					return (MutableExperiment) new MockExperiment();
				}
				throw new DatabaseException("Database Put");
			}
			};
			
			mocki[1] = new MockUp<EndExperimentTransaction>(){
				@Mock
				public void run(MutableExperiment ex) throws IllegalInputException
				{
					if(ex!=null)
					{
						return;
					}
					throw new IllegalInputException("fasfd", "asdf");
				}
			};
			connector.endExperiment("Test");
	}
	
	/**
	 * Testet ob bei der Methode endExperiment alle nötigen Methodenaufrufe getätigt werden und unter der vorraussetzung einer nicht funktionierenden Modellschicht funktioniert.
	*/
	@Test(expected=ModelException.class)
	public void endExperimentDatabase() throws ModelException
	{
		GlobalApplicationConfig.configureFromServletContext(null);
		
		mocki[0] = new MockUp<PersistentExperimentRepo>(){
			@Mock
			MutableExperiment loadExperiment(String name) throws DatabaseException{
				if(name == "Test")
				{
					return (MutableExperiment) new MockExperiment();
				}
				throw new DatabaseException("Database Put");
			}
			};
			
			mocki[1] = new MockUp<EndExperimentTransaction>(){
				@Mock
				public void run(MutableExperiment ex) throws IllegalInputException
				{
					if(ex!=null)
					{
						return;
					}
					throw new IllegalInputException("fasfd", "asdf");
				}
			};
			connector.endExperiment("Test1");
	}
	
	/**
	 * Testet ob bei der Methode endExperiment alle nötigen Methodenaufrufe getätigt werden und unter der vorraussetzung einer nicht funktionierenden Modellschicht funktioniert.
	*/
	@Test(expected=ModelException.class)
	public void endExperimentIllegalInput() throws ModelException
	{
		GlobalApplicationConfig.configureFromServletContext(null);
		
		mocki[0] = new MockUp<PersistentExperimentRepo>(){
			@Mock
			MutableExperiment loadExperiment(String name) throws DatabaseException{
				if(name == "Test")
				{
					return (MutableExperiment) new MockExperiment();
				}
				throw new DatabaseException("Database Put");
			}
			};
			
			mocki[1] = new MockUp<EndExperimentTransaction>(){
				@Mock
				public void run(MutableExperiment ex) throws IllegalInputException
				{
					throw new IllegalInputException("fasfd", "asdf");
				}
			};
			connector.endExperiment("Test");
	}
	
	/**
	 * Testet ob bei der Methode deleteExperiment alle nötigen Methodenaufrufe getätigt werden und unter der vorraussetzung einer funktionierenden Modellschicht funktioniert.
	*/
	@Test
	public void deleteExperimentWorking() throws ModelException
	{
		GlobalApplicationConfig.configureFromServletContext(null);
		
		mocki[0] = new MockUp<PersistentExperimentRepo>(){
			@Mock
			MutableExperiment loadExperiment(String name) throws DatabaseException{
				if(name == "Test")
				{
					return (MutableExperiment) new MockExperiment();
				}
				throw new DatabaseException("Database Put");
			}
			@Mock
			public void deleteExperiment(String name) throws DatabaseException{
				if(name != "Test")
				{
					throw new DatabaseException("Data Tot");
				}
			}
			};
			
			mocki[1] = new MockUp<MockExperiment>(){
			@Mock
			public boolean isFinished(){
				return true;
			}
		};
		

		connector.deleteExperiment("Test");
		
	}
	
	/**
	 * Testet ob bei der Methode deleteExperiment alle nötigen Methodenaufrufe getätigt werden und unter der vorraussetzung einer nicht funktionierenden Modellschicht funktioniert.
	*/
	@Test(expected=ModelException.class)
	public void deleteExperimentException() throws ModelException
	{
		GlobalApplicationConfig.configureFromServletContext(null);
		
		mocki[0] = 	new MockUp<PersistentExperimentRepo>(){
			@Mock
			MutableExperiment loadExperiment(String name) throws DatabaseException{
				if(name != "Test")
				{
					return (MutableExperiment) new MockExperiment();
				}
				throw new DatabaseException("Database Put");
			}
			@Mock
			public void deleteExperiment(String name) throws DatabaseException{
				if(name != "Test")
				{
					throw new DatabaseException("Data Tot");
				}
			}
			};
			
			mocki[1] = 	new MockUp<MockExperiment>(){
			@Mock
			public boolean isFinished(){
				return true;
			}
		};
		

		connector.deleteExperiment("Test");
		
	}
	
	/**
	 * Testet ob bei der Methode findInternalAssignmentIdWorking alle nötigen Methodenaufrufe getätigt werden und unter der vorraussetzung einer funktionierenden Modellschicht funktioniert.
	*/
	@Test
	public void findInternalAssignmentIdWorking() throws ModelException{
		
		GlobalApplicationConfig.configureFromServletContext(null);
		
		mocki[0] = new MockUp<PersistentExperimentRepo>(){
			@Mock
			MutableExperiment loadExperiment(String name) throws DatabaseException{
				if(name == "Test")
				{
					return (MutableExperiment) new MockExperiment();
				}
				throw new DatabaseException("Database Put");
			}
		};
		
		mocki[1] = new MockUp<MockExperiment>(){
			@Mock
			public Assignment getAssignmentWithMturkId(AssignmentId mtrkid){
				if(mtrkid!=null)
				{
					return (Assignment) new MockAssignment();
				}
				return null;
			}
		};
		
		mocki[2] = new MockUp<MockAssignment>(){
			@Mock
			public String getID(){
				return "TestId";
			}
		};
		
		assertTrue(connector.findInternalAssignmentId(new AssignmentId("asi"), "Test").equals("TestId"));
	}
	
	/**
	 * Testet ob bei der Methode findInternalAssignmentId alle nötigen Methodenaufrufe getätigt werden und unter der vorraussetzung einer funktionierenden ohne Assignments Modellschicht funktioniert.
	*/
	@Test
	public void findInternalAssignmentIdNoAssignment() throws ModelException{
		
		GlobalApplicationConfig.configureFromServletContext(null);
		
		mocki[0] = new MockUp<PersistentExperimentRepo>(){
			@Mock
			MutableExperiment loadExperiment(String name) throws DatabaseException{
				if(name == "Test")
				{
					return (MutableExperiment) new MockExperiment();
				}
				throw new DatabaseException("Database Put");
			}
		};
		
		mocki[1] = new MockUp<MockExperiment>(){
			@Mock
			public Assignment getAssignmentWithMturkId(AssignmentId mtrkid){
				if(mtrkid!=null)
				{
					return (Assignment) new MockAssignment();
				}
				return null;
			}
			@Mock
			public MutableAssignment addAssignment(){
				return (MutableAssignment) new MockAssignment();
			}
		};
		
		mocki[2] = new MockUp<MockAssignment>(){
			@Mock
			public String getID(){
				return "TestId";
			}
			@Mock
			public void setAssignmentID(AssignmentId id)
			{
				
			}
		};
		
		assertTrue(connector.findInternalAssignmentId(null, "Test").equals("TestId"));
	}
	
	/**
	 * Testet ob bei der Methode findInternalAssignmentId alle nötigen Methodenaufrufe getätigt werden und unter der vorraussetzung einer falschen Eingabe funktioniert.
	*/
	@Test(expected=ModelException.class)
	public void findInternalAssignmentIdException() throws ModelException{
		
		GlobalApplicationConfig.configureFromServletContext(null);
		
		mocki[0] = new MockUp<PersistentExperimentRepo>(){
			@Mock
			MutableExperiment loadExperiment(String name) throws DatabaseException{
				if(name == "Test")
				{
					return (MutableExperiment) new MockExperiment();
				}
				throw new DatabaseException("Database Put");
			}
		};
		
		mocki[1] = new MockUp<MockExperiment>(){
			@Mock
			public Assignment getAssignmentWithMturkId(AssignmentId mtrkid){
				if(mtrkid!=null)
				{
					return (Assignment) new MockAssignment();
				}
				return null;
			}
		};
		
		mocki[2] = new MockUp<MockAssignment>(){
			@Mock
			public String getID(){
				return "TestId";
			}
		};
		
		connector.findInternalAssignmentId(new AssignmentId("asi"), "Test123").equals("TestId");
	}
	
	/**
	 * Testet ob bei der Methode increasePreviewClickCount alle nötigen Methodenaufrufe getätigt werden und unter der vorraussetzung einer funktionierenden Modellschicht funktioniert.
	*/
	@Test
	public void increasePreviewClickCount() throws ModelException{
		
		GlobalApplicationConfig.configureFromServletContext(null);
		
		mocki[0] = new MockUp<PersistentExperimentRepo>(){
			@Mock
			MutableExperiment loadExperiment(String name) throws DatabaseException{
				if(name == "Test")
				{
					return (MutableExperiment) new MockExperiment();
				}
				throw new DatabaseException("Database Put");
			}
		};
		
		mocki[1] = new MockUp<MockExperiment>(){
			@Mock
			public MutableStats getStats(){
				return (MutableStats) new MockStats();
			}
		};
		
		mocki[2] = new MockUp<MockStats>(){
			@Mock
			public void addPreviewClick()
			{
				
			}
		};
		
		connector.increasePreviewClickCount("Test");
		
	}
	/**
	 * Testet ob bei der Methode increasePreviewClickCount alle nötigen Methodenaufrufe getätigt werden und unter der vorraussetzung einer falschen Eingabe funktioniert.
	*/
	@Test(expected=ModelException.class)
	public void increasePreviewClickCountException() throws ModelException{
		
		GlobalApplicationConfig.configureFromServletContext(null);
		
		mocki[0] = new MockUp<PersistentExperimentRepo>(){
			@Mock
			MutableExperiment loadExperiment(String name) throws DatabaseException{
				if(name == "Test")
				{
					return (MutableExperiment) new MockExperiment();
				}
				throw new DatabaseException("Database Put");
			}
		};
		
		mocki[1] = new MockUp<MockExperiment>(){
			@Mock
			public MutableStats getStats(){
				return (MutableStats) new MockStats();
			}
		};
		
		mocki[2] = new MockUp<MockStats>(){
			@Mock
			public void addPreviewClick()
			{
				
			}
		};
		
		connector.increasePreviewClickCount("Test123");
		
	}
	
}
