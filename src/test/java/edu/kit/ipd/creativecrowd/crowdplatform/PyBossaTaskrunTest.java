package edu.kit.ipd.creativecrowd.crowdplatform;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class PyBossaTaskrunTest {

	static PyBossaTaskrun pbtr;
	
	@BeforeClass
	public static void setUp() {
		pbtr = new PyBossaTaskrun();
	}
	@Test
	public void testSetAndGetInfo() {
		pbtr.setInfo(42);
		assertEquals(42, pbtr.getInfo());
	}

	@Test
	public void testSetAndGetUser_id() {
		pbtr.setUser_id(42);
		assertEquals(42, pbtr.getUser_id());
	}

	@Test
	public void testSetAndGetUser_ip() {
		pbtr.setUser_ip(42);
		assertEquals(42, pbtr.getUser_ip());
	}

	@Test
	public void testSetAndGetProject_id() {
		pbtr.setProject_id(42);
		assertEquals(42, pbtr.getProject_id());
	}

	@Test
	public void testSetAndGetTask_id() {
		pbtr.setTask_id(42);
		assertEquals(42, pbtr.getTask_id());
	}

	@Test
	public void testSetAndGetId() {
		pbtr.setId(42);
		assertEquals(42, pbtr.getId());
	}

}
