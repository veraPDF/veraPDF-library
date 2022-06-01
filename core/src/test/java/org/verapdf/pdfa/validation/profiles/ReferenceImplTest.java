/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
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
/**
 * 
 */
package org.verapdf.pdfa.validation.profiles;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
@SuppressWarnings("static-method")
public class ReferenceImplTest {
	private final static String DEFAULT_REFERENCE_STRING = "Reference [specification=specification, clause=clause]";

	/**
	 * Test method for
	 * {@link org.verapdf.pdfa.validation.ReferenceImpl#equals(java.lang.Object)}.
	 */
	@Test
	public final void testEqualsObject() {
		EqualsVerifier.forClass(ReferenceImpl.class).verify();
	}

	/**
	 * Test method for
	 * {@link org.verapdf.pdfa.validation.ReferenceImpl#toString()}.
	 */
	@Test
	public final void testToString() {
		assertEquals(DEFAULT_REFERENCE_STRING, Profiles.defaultReference().toString());
	}

	/**
	 * Test method for
	 * {@link org.verapdf.pdfa.validation.ReferenceImpl#fromValues(String, String)}.
	 */
	@Test
	public final void testFromValues() {
		// Get an equivalent to the default instance
		Reference reference = Profiles.referenceFromValues("specification", "clause");
		Reference defaultInstance = Profiles.defaultReference();
		// Equivalent is NOT the same object as default instance
		assertNotSame(reference, defaultInstance);
		// But it is equal
		assertEquals(reference, defaultInstance);
	}

	/**
	 * Test method for
	 * {@link org.verapdf.pdfa.validation.ReferenceImpl#fromReference(org.verapdf.pdfa.validation.Reference)}.
	 */
	@Test
	public final void testFromReference() {
		Reference defaultInstance = Profiles.defaultReference();
		// Get an equivalent to the default instance
		Reference reference = ReferenceImpl.fromValues(defaultInstance.getSpecification(), defaultInstance.getClause());
		// Equivalent is NOT the same object as default instance
		assertNotSame(reference, defaultInstance);
		// But it is equal
		assertEquals(reference, defaultInstance);
	}
}
