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
package org.verapdf.pdfa.results;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.verapdf.core.XmlSerialiser;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.validation.profiles.Profiles;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.nio.file.Files;
import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
@SuppressWarnings("static-method")
public class ValidationResultTest {
	private static final String DEFAULT_RESULT_STRING = "ValidationResult [flavour=" + PDFAFlavour.NO_FLAVOUR //$NON-NLS-1$
			+ ", totalAssertions=" + 0 + ", assertions=" + Collections.<TestAssertion>emptyList() + ", isCompliant=" //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
			+ false + "]"; //$NON-NLS-1$

	/**
	 * Test method for
	 * {@link org.verapdf.pdfa.results.ValidationResultImpl#hashCode()}.
	 */
	@Test
	public final void testHashCodeAndEquals() {
		EqualsVerifier.forClass(ValidationResultImpl.class).withIgnoredFields("profileDetails", "validationProfile", "failedChecks").suppress(Warning.NULL_FIELDS, Warning.NONFINAL_FIELDS).verify();
	}

	/**
	 * Test method for
	 * {@link org.verapdf.pdfa.results.ValidationResultImpl#toString()}.
	 */
	@Test
	public final void testToString() {
		assertTrue(ValidationResults.defaultResult().toString().equals(DEFAULT_RESULT_STRING));
	}

	/**
	 * Test method for
	 * {@link org.verapdf.pdfa.results.ValidationResultImpl#defaultInstance()}.
	 */
	@Test
	public final void testDefaultInstance() {
		ValidationResult defaultResult = ValidationResults.defaultResult();
		assertTrue(defaultResult.equals(ValidationResults.defaultResult()));
		assertTrue(defaultResult == ValidationResults.defaultResult());
	}

	/**
	 * Test method for
	 * {@link org.verapdf.pdfa.results.ValidationResultImpl#fromValues(org.verapdf.pdfa.validation.profiles.ValidationProfile, java.util.Set, boolean, int)}.
	 */
	@Test
	public final void testFromValues() {
		ValidationResult resultFromVals = ValidationResults.resultFromValues(Profiles.defaultProfile(), Collections.<TestAssertion>emptyList(), false);
		assertTrue(resultFromVals.equals(ValidationResults.defaultResult()));
		assertFalse(resultFromVals == ValidationResults.defaultResult());
	}

	/**
	 * Test method for
	 * {@link org.verapdf.pdfa.results.ValidationResultImpl#fromValidationResult(ValidationResult)}.
	 */
	@Test
	public final void testFromValidationResult() {
		ValidationResult resultFromResult = ValidationResultImpl
				.fromValidationResult(ValidationResults.defaultResult());
		assertTrue(resultFromResult.equals(ValidationResults.defaultResult()));
		assertFalse(resultFromResult == ValidationResults.defaultResult());
	}

	/**
	 * Test method for
	 * {@link org.verapdf.pdfa.results.ValidationResultImpl#toXml(org.verapdf.pdfa.results.ValidationResult, java.lang.Boolean)}.
	 * 
	 * @throws IOException
	 * @throws JAXBException
	 */
	@Test
	public final void testToXmlString() throws JAXBException {
		List<TestAssertion> assertions = new ArrayList<>();
		assertions.add(ValidationResults.defaultAssertion());
		ValidationResult result = ValidationResults.resultFromValues(Profiles.defaultProfile(), assertions);
		String xmlRawResult = XmlSerialiser.toXml(result, true, false);
		String xmlPrettyResult = XmlSerialiser.toXml(result, true, true);
		assertFalse(xmlRawResult.equals(xmlPrettyResult));
		ValidationResult fromRawXml = XmlSerialiser.typeFromXml(ValidationResultImpl.class, xmlRawResult);
		ValidationResult fromPrettyXml = XmlSerialiser.typeFromXml(ValidationResultImpl.class, xmlPrettyResult);
		assertTrue(fromRawXml.equals(fromPrettyXml));
		assertTrue(fromRawXml.equals(result));
	}

	/**
	 * Test method for
	 * {@link org.verapdf.pdfa.results.ValidationResultImpl#fromXml(java.io.InputStream)}.
	 * 
	 * @throws IOException
	 * @throws JAXBException
	 */
	@Test
	public final void testFromXmlInputStream() throws IOException, JAXBException {
		List<TestAssertion> assertions = new ArrayList<>();
		assertions.add(TestAssertionImpl.defaultInstance());
		ValidationResult result = ValidationResults.resultFromValues(Profiles.defaultProfile(), assertions);
		File temp = Files.createTempFile("profile", "xml").toFile(); //$NON-NLS-1$ //$NON-NLS-2$
		try (OutputStream forXml = new FileOutputStream(temp)) {
			XmlSerialiser.toXml(result, forXml, true, true);
		}
		try (InputStream readXml = new FileInputStream(temp)) {
			ValidationResult unmarshalledResult = XmlSerialiser.typeFromXml(ValidationResultImpl.class, readXml);
			assertFalse(result == unmarshalledResult);
			assertTrue(result.equals(unmarshalledResult));
		}
		temp.delete();
	}

	/**
	 * Test method for
	 * {@link org.verapdf.pdfa.results.ValidationResultImpl#fromXml(java.io.InputStream)}.
	 * 
	 * @throws IOException
	 * @throws JAXBException
	 */
	@Test
	public final void testFromXmlInputString() throws JAXBException {
		List<TestAssertion> assertions = new ArrayList<>();
		assertions.add(TestAssertionImpl.defaultInstance());
		ValidationResult result = ValidationResults.resultFromValues(Profiles.defaultProfile(), assertions);
		String xmlSource = XmlSerialiser.toXml(result, true, true);
		ValidationResult unmarshalledResult = ValidationResults.resultFromXmlString(xmlSource);
		assertFalse(result == unmarshalledResult);
		assertTrue(result.equals(unmarshalledResult));
	}
}
