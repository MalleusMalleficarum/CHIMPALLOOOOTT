package edu.kit.ipd.creativecrowd.database;

import static org.junit.Assert.*;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.creativecrowd.database.SQLiteDatabaseConnection;
import edu.kit.ipd.creativecrowd.database.Value;

public class SQLiteDatabaseConnectionTest {

     SQLiteDatabaseConnection testConnection = SQLiteDatabaseConnection.getInstance();
     File file = new File("CreativeCrowd.db");

     /**
      * erstellt eine neue Verbindung zur Datenbank und eine neue Tabelle
      */
     @Before
     public void setUpDatabaseConnectionTest() {
          file.delete();
          try {
               testConnection.setUpDatabaseConnection("jdbc:sqlite:CreativeCrowd.db");
               testConnection.query("CREATE TABLE IF NOT EXISTS TOLLETABLE" + "(ID INT PRIMARY KEY     NOT NULL," + " NAME           TEXT    NOT NULL, " + " AGE            INT     NOT NULL, "
                       + " ADDRESS       TEXT, " + " SALARY         REAL)");
          } catch (Exception e) {
               fail(e.getClass().getName() + ": " + e.getMessage());

          }
     }
     
  

     /**
      *versucht, einen INSERT-query in die erstellte Tabelle durchzuführen, failt bei Exception
      */
     @Test
     public void queryInsertTest() {
          try {
        	  List<Value> list  = new ArrayList<Value>();
        	  list.add(Value.fromInt(2));
        	  list.add(Value.fromString("Paul"));
        	  list.add(Value.fromInt(32));
        	  list.add(Value.fromString("California"));
        	  list.add(Value.fromInt(20000));
        	  
               testConnection.query(testConnection.formatString("INSERT INTO TOLLETABLE (ID,NAME,AGE,ADDRESS,SALARY) VALUES (?,?,?,?,?);",list));
               List<Iterable<Value>> table = (List<Iterable<Value>>) testConnection.query("SELECT * FROM TOLLETABLE");
               assertTrue(table.iterator().next().iterator().next().asInt() == 2);
				testConnection.query(testConnection.formatString("DELETE FROM TOLLETABLE WHERE ID=?;",Value.fromInt(2)));

          } catch (SQLException e) {
               fail(e.getClass().getName() + ": " + e.getMessage() + ": " + e.getStackTrace());

          }
     }

     /**
      * formatiert ein String mit den Werten eines Floats, eines Ints und eines anderen Strings und überprüft, ob die Werte korrekt ins Ziel-String eingebunden wurden
      */
     @Test 
     public void formatStringTest1()  {
    	 List<Value> list = new LinkedList<Value>();
    	 list.add(Value.fromFloat(1.0f));
    	 list.add(Value.fromInt(2));
    	 list.add(Value.fromString("drei"));
    	 String test = "Eins ? Zwei ? Drei ?";
         String result = "";
		try {
			result = testConnection.formatString(test,list );
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      assertTrue(result.equals("Eins 1.0 Zwei 2 Drei 'drei'"));
     }
   
     
     /**
      * generiert zwei ids und überprüft, ob sie sich unterscheiden
      */
     @Test 
     public void generateIDTest() {
    	 String id = testConnection.generateID("TOLLETABLE");
    	 assertFalse(id.equals(testConnection.generateID("TOLLETABLE")));
     }
     
     /**
      * erzeugt ein paar Values und testet sie auf korrekt gesetzte Attribute 
      */
     @Test
     public void ValueTest() {
    	 assertTrue(Value.fromInt(2).asInt() == 2);
    	 assertTrue(Value.fromLong(2).asLong() == 2);
    	 assertTrue(Value.fromString("apurgh334").asString().equals("apurgh334"));
     }

}