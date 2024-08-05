/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2024, veraPDF Consortium <info@verapdf.org>
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

import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.verapdf.extensions.ExtensionObjectType;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.EnumSet;
import java.util.logging.Level;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 30 Oct 2016:00:46:37
 */

@SuppressWarnings("static-method")
public class ValidatorConfigTest {

	/**
	 * Test method for
	 * {@link org.verapdf.pdfa.validation.validators.ValidatorConfigImpl#hashCode()}.
	 */
	@Test
	public final void testHashCodeAndEquals() {
		EqualsVerifier.forClass(ValidatorConfigImpl.class).suppress(Warning.NONFINAL_FIELDS).verify();
	}

	/**
	 * Test method for
	 * {@link org.verapdf.pdfa.validation.validators.ValidatorConfigImpl#defaultInstance()}.
	 */
	@Test
	public final void testDefaultInstance() {
		ValidatorConfig defaultInstance = ValidatorFactory.defaultConfig();
		assertSame(defaultInstance, ValidatorFactory.defaultConfig());
	}

	/**
	 * Test method for
	 * {@link ValidatorFactory#createConfig(PDFAFlavour, PDFAFlavour, boolean, int, boolean, boolean, Level, int, boolean, String, boolean, boolean, EnumSet)} (PDFAFlavour, PDFAFlavour, boolean, int, boolean, boolean, Level, int, boolean, String, boolean, boolean)}.
	 */
	@Test
	public final void testFromValues() {
		ValidatorConfig defaultInstance = ValidatorFactory.defaultConfig();
		ValidatorConfig fromVals = ValidatorFactory.createConfig(defaultInstance.getFlavour(),
				defaultInstance.getDefaultFlavour(), defaultInstance.isRecordPasses(), defaultInstance.getMaxFails(),
				defaultInstance.isDebug(), false, Level.WARNING, defaultInstance.getMaxNumberOfDisplayedFailedChecks(),
				defaultInstance.showErrorMessages(), "", false, false, EnumSet.noneOf(ExtensionObjectType.class));
		assertEquals(fromVals, defaultInstance);
		assertNotSame(fromVals, defaultInstance);
	}

}
