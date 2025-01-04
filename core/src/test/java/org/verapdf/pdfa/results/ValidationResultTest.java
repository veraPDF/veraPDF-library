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
import org.verapdf.pdfa.validation.profiles.ValidationProfile;
import org.verapdf.processor.reports.enums.JobEndStatus;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.nio.file.Files;
import java.util.*;

import static org.junit.Assert.*;

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
		assertEquals(DEFAULT_RESULT_STRING, ValidationResults.defaultResult().toString());
	}

	/**
	 * Test method for
	 * {@link org.verapdf.pdfa.results.ValidationResultImpl#defaultInstance()}.
	 */
	@Test
	public final void testDefaultInstance() {
		ValidationResult defaultResult = ValidationResults.defaultResult();
		assertEquals(defaultResult, ValidationResults.defaultResult());
		assertSame(defaultResult, ValidationResults.defaultResult());
	}

	/**
	 * Test method for
	 * {@link org.verapdf.pdfa.results.ValidationResults#resultFromValues(ValidationProfile, List, boolean, JobEndStatus)}.
	 */
	@Test
	public final void testFromValues() {
		ValidationResult resultFromVals = ValidationResults.resultFromValues(Profiles.defaultProfile(), Collections.emptyList(), false, JobEndStatus.NORMAL);
		assertEquals(resultFromVals, ValidationResults.defaultResult());
		assertNotSame(resultFromVals, ValidationResults.defaultResult());
	}

	/**
	 * Test method for
	 * {@link org.verapdf.pdfa.results.ValidationResultImpl#fromValidationResult(ValidationResult)}.
	 */
	@Test
	public final void testFromValidationResult() {
		ValidationResult resultFromResult = ValidationResultImpl
				.fromValidationResult(ValidationResults.defaultResult());
		assertEquals(resultFromResult, ValidationResults.defaultResult());
		assertNotSame(resultFromResult, ValidationResults.defaultResult());
	}

	/**
	 * Test method for
	 * {@link XmlSerialiser#toXml(Object, boolean, boolean)}.
	 * 
	 * @throws IOException
	 * @throws JAXBException
	 */
	@Test
	public final void testToXmlString() throws JAXBException {
		List<TestAssertion> assertions = new ArrayList<>();
		assertions.add(ValidationResults.defaultAssertion());
		ValidationResult result = ValidationResults.resultFromValues(Profiles.defaultProfile(), assertions, JobEndStatus.NORMAL);
		String xmlRawResult = XmlSerialiser.toXml(result, true, false);
		String xmlPrettyResult = XmlSerialiser.toXml(result, true, true);
		assertNotEquals(xmlRawResult, xmlPrettyResult);
		ValidationResult fromRawXml = XmlSerialiser.typeFromXml(ValidationResultImpl.class, xmlRawResult);
		ValidationResult fromPrettyXml = XmlSerialiser.typeFromXml(ValidationResultImpl.class, xmlPrettyResult);
		assertEquals(fromRawXml, fromPrettyXml);
		assertEquals(fromRawXml, result);
	}

	/**
	 * Test method for
	 * {@link XmlSerialiser#typeFromXml(Class, InputStream)}.
	 * 
	 * @throws IOException
	 * @throws JAXBException
	 */
	@Test
	public final void testFromXmlInputStream() throws IOException, JAXBException {
		List<TestAssertion> assertions = new ArrayList<>();
		assertions.add(TestAssertionImpl.defaultInstance());
		ValidationResult result = ValidationResults.resultFromValues(Profiles.defaultProfile(), assertions, JobEndStatus.NORMAL);
		File temp = Files.createTempFile("profile", "xml").toFile(); //$NON-NLS-1$ //$NON-NLS-2$
		try (OutputStream forXml = new FileOutputStream(temp)) {
			XmlSerialiser.toXml(result, forXml, true, true);
		}
		try (InputStream readXml = new FileInputStream(temp)) {
			ValidationResult unmarshalledResult = XmlSerialiser.typeFromXml(ValidationResultImpl.class, readXml);
			assertNotSame(result, unmarshalledResult);
			assertEquals(result, unmarshalledResult);
		}
		temp.delete();
	}

	/**
	 * Test method for
	 * {@link ValidationResults#resultFromXmlString(String)}.
	 * 
	 * @throws IOException
	 * @throws JAXBException
	 */
	@Test
	public final void testFromXmlInputString() throws JAXBException {
		List<TestAssertion> assertions = new ArrayList<>();
		assertions.add(TestAssertionImpl.defaultInstance());
		ValidationResult result = ValidationResults.resultFromValues(Profiles.defaultProfile(), assertions, JobEndStatus.NORMAL);
		String xmlSource = XmlSerialiser.toXml(result, true, true);
		ValidationResult unmarshalledResult = ValidationResults.resultFromXmlString(xmlSource);
		assertNotSame(result, unmarshalledResult);
		assertEquals(result, unmarshalledResult);
	}
}
