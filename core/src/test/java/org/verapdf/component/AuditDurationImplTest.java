/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2025, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library core as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.component;

import java.util.Arrays;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

import static org.junit.Assert.*;

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
		assertSame(defaultInstance, AuditDurationImpl.defaultInstance());
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
