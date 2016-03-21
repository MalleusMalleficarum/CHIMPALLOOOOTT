package edu.kit.ipd.creativecrowd.crowdplatform;

import static org.junit.Assert.*;

import org.junit.Test;

public class PlatformIdentityTest {

	@Test
	public void testGetIdentityFromPrefix() {
		assertEquals(PlatformIdentity.MTurk, PlatformIdentity.getIdentityFromPrefix("MT Rushmore"));
		assertEquals(PlatformIdentity.MTurk, PlatformIdentity.getIdentityFromPrefix("MTPBMTPB"));
		assertEquals(PlatformIdentity.PyBossa, PlatformIdentity.getIdentityFromPrefix("PB is heavy"));
		assertEquals(PlatformIdentity.Unspecified, PlatformIdentity.getIdentityFromPrefix("UN Sicherheits Rad ab"));
		assertEquals(PlatformIdentity.Unspecified, PlatformIdentity.getIdentityFromPrefix("Ausdrickst hob i di"));
		assertEquals(PlatformIdentity.Unspecified, PlatformIdentity.getIdentityFromPrefix("MPBP"));
	}

}
