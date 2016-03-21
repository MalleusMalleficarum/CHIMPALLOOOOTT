package edu.kit.ipd.creativecrowd.persistentmodel;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.kit.ipd.chimpalot.util.GlobalApplicationConfig;
import edu.kit.ipd.creativecrowd.readablemodel.AmazonVoucher;
/**
 * 
 * @author Robin
 *
 */
public class PersistentAmazonVoucherTest {
	static PersistentAmazonVoucherRepo rep;
	PersistentAmazonVoucher voucher;
	String id;
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
	public static void tearDownAfterClass()  {
	}

	@Before
	public void setUp() {
		try {
			rep.addVoucher("Hallo Welt", 25);
			voucher = (PersistentAmazonVoucher) rep.getVoucherWorth(25);
			id = voucher.getID();
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
		
	}

	@After
	public void tearDown() {
		try {
			rep.useVoucher(id);
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void illEagleCode() {
		try {
			assertFalse(voucher.setCode(null));
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void legalCode() {
		try {
			assertTrue(voucher.setCode("duckduckgo"));
			assertEquals(voucher.getCode(), "duckduckgo");
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
		
	}
	@Test
	public void illegalValue() {
		try {
			assertFalse(voucher.setValue(-11));
			assertFalse(voucher.setValue(0));
			assertEquals(voucher.getValue(), 25);
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void legalValue() {
		try {
			assertTrue(voucher.setValue(9001));
			assertEquals(voucher.getValue(), 9001);
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void compareAgainstSelf() {
		assertEquals(voucher.compareTo(voucher), 0);
	}
	/**
	 * Note that the comparator sorts upside down
	 */
	@Test
	public void compareAgainstGreater() {
		try {
			rep.addVoucher("hlo", 90);
			AmazonVoucher vm = rep.getVoucherWorth(90);
			assertEquals(voucher.compareTo(vm), 1);
			rep.useVoucher(vm.getID());
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
		
	}
	@Test
	public void compareAgainstSmaller() {
		try {
			rep.addVoucher("hlo", 90);
			AmazonVoucher vm = rep.getVoucherWorth(90);
			assertEquals(vm.compareTo(voucher), -1);
			rep.useVoucher(vm.getID());
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}

}
