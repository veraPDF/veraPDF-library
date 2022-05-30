package org.verapdf.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

public class AuditDurationImplTest {
	/**
	 * Test method for
	 * {@link org.verapdf.component.AuditDurationImpl#hashCode()}.
	 */
	@Test
	public void testHashCodeAndEquals() {
		EqualsVerifier.forClass(AuditDurationImpl.class).verify();
	}

	@Test
	public void testDefaultInstance() {
		AuditDuration defaultInstance = AuditDurationImpl.defaultInstance();
		assertTrue(defaultInstance == AuditDurationImpl.defaultInstance());
	}

	@Test(expected=IllegalArgumentException.class)
	public void testFromValuesSubZeroStart() {
		AuditDurationImpl.fromValues(-10, 0);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testFromValuesSubZeroFinish() {
		AuditDurationImpl.fromValues(0, -15);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testFromValuesHorseBeforeCart() {
		AuditDurationImpl.fromValues(250000, 200000);
	}

	@Test
	public void testGetStringDuration() {
		long second = 1000;
		long minute = 60 * second;
		long hour = 60 * minute;
		assertEquals("00:00:00.000", AuditDurationImpl.getStringDuration(0));
		assertEquals("01:23:45.678", AuditDurationImpl.getStringDuration(hour + 23 * minute + 45 * second + 678));
	}

	@Test
	public void testSumNulllDurations() {
		assertEquals(AuditDurationImpl.defaultInstance(), AuditDurationImpl.sumDuration(null));
	}

	@Test
	public void testSumEqualDurations() {
		AuditDuration first = AuditDurationImpl.fromValues(10000, 25000);
		AuditDuration second = AuditDurationImpl.fromValues(10000, 25000);
		AuditDuration[] durations = {first, second};
		assertEquals(first, AuditDurationImpl.sumDuration(Arrays.asList(durations)));
		assertEquals(second, AuditDurationImpl.sumDuration(Arrays.asList(durations)));
	}

	@Test
	public void testSumOverlappingDurations() {
		AuditDuration first = AuditDurationImpl.fromValues(14000, 25000);
		AuditDuration second = AuditDurationImpl.fromValues(15000, 30000);
		AuditDuration third = AuditDurationImpl.fromValues(12000, 35000);
		AuditDuration fourth = AuditDurationImpl.fromValues(10000, 27000);
		AuditDuration expected = AuditDurationImpl.fromValues(10000, 35000);
		AuditDuration[] durations = {first, second, third, fourth};
		assertEquals(expected, AuditDurationImpl.sumDuration(Arrays.asList(durations)));
	}
}
