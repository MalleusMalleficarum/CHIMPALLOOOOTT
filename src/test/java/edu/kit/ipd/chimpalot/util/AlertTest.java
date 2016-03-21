package edu.kit.ipd.chimpalot.util;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class AlertTest {

	@Test
	public void testRegisterRequester() {
		Alert.registerRequester("chimpalot.reward@gmail.com");
		List<String> test2 = new LinkedList<String>();
		test2.add("chimpalot.reward@gmail.com");
		assertEquals(test2, Alert.getRegisteredMails());
		Alert.registerRequester("chimpalot.reward@gmail.com");
		assertEquals(test2, Alert.getRegisteredMails());
		Alert.deregisterRequester("chimpalot.reward@gmail.com");
	}

	@Test
	public void testDeregisterRequester() {
		Alert.registerRequester("chimpalot.reward@gmail.com");
		Alert.deregisterRequester("chimpalot.reward@gmail.com");
		List<String> empty = new LinkedList<String>();
		assertEquals(empty, Alert.getRegisteredMails());
	}

	@Test
	public void testGetRegisteredMails() {
		List<String> test = new LinkedList<String>();
		assertEquals(test, Alert.getRegisteredMails());
		Alert.registerRequester("chimpalot.reward@gmail.com");
		test.add("chimpalot.reward@gmail.com");
		assertEquals(test, Alert.getRegisteredMails());
		Alert.deregisterRequester("chimpalot.reward@gmail.com");
	}

	//Notify methods only call Email.send which is tested in another class
	@Test
	public void testAllNotify() {
		return;
	}

}
