package edu.kit.ipd.creativecrowd.persistentmodel;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.kit.ipd.chimpalot.util.GlobalApplicationConfig;
import edu.kit.ipd.chimpalot.util.Logger;
import edu.kit.ipd.creativecrowd.readablemodel.AmazonVoucher;

public class PersistentAmazonVoucherRepoTest {
	static PersistentAmazonVoucherRepo rep;

	@BeforeClass
	public static void setUpBeforeClass() {
		File file = new File("CreativeCrowd.db");
		file.delete();
		GlobalApplicationConfig.configure(true);
		
		try {
			rep = new PersistentAmazonVoucherRepo();
			
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}

	@AfterClass
	public static void tearDownAfterClass(){
	}

	@Before
	public void setUp(){
	}

	@After
	public void tearDown(){
		try {
			for(AmazonVoucher v : rep.getAllVouchers()) {
				rep.useVoucher(v.getID());
			}
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void insertByStringAndIntInvalid() {
		try {
			assertFalse(rep.addVoucher(null, 12));
			assertFalse(rep.addVoucher(null, -1));
			assertFalse(rep.addVoucher("MYMY", -9001));
			assertEquals(rep.getAllVouchers().size(), 0);
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
		
	}
	@Test
	public void insertByStringAndInt() {
		try {
			assertTrue(rep.addVoucher("XXXXX", 5));
			AmazonVoucher v = rep.getVoucherWorth(5);
			assertEquals(v.getCode(), "XXXXX");
			assertEquals(v.getValue(), 5);
			Logger.debug(v.getID());
			rep.useVoucher(v.getID());
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void insertByMapInvalid() {
		try {
			assertFalse(rep.addVouchers(null));
			assertEquals(rep.getAllVouchers().size(), 0);
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void insertByMapContainsInvalid() {
		Map<String, Integer> map = new HashMap<>();
		map.put(null, 1);
		map.put("Hallo", null);
		map.put(null, null);
		map.put("TOll", -1);
		try {
			rep.addVouchers(map);
			assertEquals(rep.getAllVouchers().size(), 0);
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void insertByMapValid() {
		Map<String, Integer> map = new HashMap<>();
		map.put("XY", 1);
		map.put("Schu", 20);
		try {
			rep.addVouchers(map);
			assertEquals(rep.getAllVouchers().size(), 2);
			AmazonVoucher v = rep.getVoucherWorth(20);
			assertEquals(v.getValue(), 20);
			assertEquals(v.getCode(), "Schu");
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void insertByStringInvalid() {
		try {
			assertFalse(rep.addVouchersAsString(null));
			assertFalse(rep.addVouchersAsString("TUTMIRLEIDICHHABEKEINTRENNSYMBOL"));
			assertFalse(rep.addVouchersAsString(""));
			assertFalse(rep.addVouchersAsString(";"));
			assertFalse(rep.addVouchersAsString(":"));
			assertFalse(rep.addVouchersAsString("hallo:-1"));
			assertFalse(rep.addVouchersAsString(":1"));
			assertFalse(rep.addVouchersAsString("h:"));
			assertFalse(rep.addVouchersAsString("h:10;10:-1"));
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	@Test
	public void insertByStringValid() {
		try {
			assertTrue(rep.addVouchersAsString("hallo:40"));
			assertTrue(rep.addVouchersAsString("ich:12;du:404"));
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test (expected = DatabaseException.class)
	public void valueNotHere() throws DatabaseException {

		rep.getVoucherWorth(12);
		
	}
	@Test
	public void valueNotClose() {
		try {
			rep.getVouchersWorthCloseTo(12);
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void getValueClose() {
		try {
			assertTrue(rep.addVoucher("HALLO", 5));
			List<AmazonVoucher> list = rep.getVouchersWorthCloseTo(6);
			assertEquals(list.get(0).getValue(), 5);
			assertEquals(list.size(), 1);
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
		
	}
	@Test
	public void getValueCloseGreedySuboptimal() {
		try {
			assertTrue(rep.addVoucher("HALLO", 4));
			assertTrue(rep.addVoucher("HALLO", 3));
			assertTrue(rep.addVoucher("HALLO", 3));
			List<AmazonVoucher> list = rep.getVouchersWorthCloseTo(6);
			assertEquals(list.get(0).getValue(), 4);
			assertEquals(list.size(), 1);
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void getValueClosePerfect() {
		try {
			assertTrue(rep.addVoucher("HLAO", 5));
			assertTrue(rep.addVoucher("HALLO", 4));
			assertTrue(rep.addVoucher("HALLO", 3));
			assertTrue(rep.addVoucher("HALLO", 3));
			List<AmazonVoucher> list = rep.getVouchersWorthCloseTo(4);
			assertEquals(list.get(0).getValue(), 4);
			assertEquals(list.size(), 1);
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void getID() {
		try {
			rep.addVoucher("hakmhk", 123);
			AmazonVoucher v = rep.getVoucherWorth(123);
			AmazonVoucher v2 = rep.getVoucher(v.getID());
			assertEquals(v.getValue(), v2.getValue());
			assertEquals(v.getCode(), v2.getCode());
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
		
	}
	@Test (expected = DatabaseException.class)
	public void useVoucherNotHere() throws IDNotFoundException, DatabaseException {
	
			rep.useVoucher("HALLO");
		
	}
	@Test 
	public void useVoucherHere() {
		try {
			rep.addVoucher("MUGULE", 120);
			Map<String, Integer> map = rep.useVoucher(rep.getVoucherWorth(120).getID());
			int a = map.get("MUGULE");
			assertEquals(a, 120);
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
		
	}
	

}
