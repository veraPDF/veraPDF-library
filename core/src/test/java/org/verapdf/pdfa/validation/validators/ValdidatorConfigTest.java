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
package org.verapdf.pdfa.validation.validators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 30 Oct 2016:00:46:37
 */

@SuppressWarnings("static-method")
public class ValdidatorConfigTest {

	/**
	 * Test method for
	 * {@link org.verapdf.pdfa.validation.validators.ValidatorConfigImpl#hashCode()}.
	 */
	@Test
	public final void testHashCodeAndEquals() {
		EqualsVerifier.forClass(ValidatorConfigImpl.class).verify();
	}

	/**
	 * Test method for
	 * {@link org.verapdf.pdfa.validation.validators.ValidatorConfigImpl#defaultInstance()}.
	 */
	@Test
	public final void testDefaultInstance() {
		ValidatorConfig defaultInstance = ValidatorFactory.defaultConfig();
		assertTrue(defaultInstance == ValidatorFactory.defaultConfig());
	}

	/**
	 * Test method for
	 * {@link org.verapdf.pdfa.validation.validators.ValidatorConfigImpl#fromValues(org.verapdf.pdfa.flavours.PDFAFlavour, boolean, int, int)}.
	 */
	@Test
	public final void testFromValues() {
		ValidatorConfig defaultInstance = ValidatorFactory.defaultConfig();
		ValidatorConfig fromVals = ValidatorFactory.createConfig(defaultInstance.getFlavour(),
				defaultInstance.isRecordPasses(), defaultInstance.getMaxFails());
		assertTrue(fromVals.equals(defaultInstance));
		assertFalse(fromVals == defaultInstance);
	}

}
