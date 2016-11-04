/**
 * 
 */
package org.verapdf.pdfa.results;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.verapdf.core.XmlSerialiser;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
@SuppressWarnings("static-method")
public class ValidationResultTest {
	private static final String DEFAULT_RESULT_STRING = "ValidationResult [flavour=" + PDFAFlavour.NO_FLAVOUR //$NON-NLS-1$
			+ ", totalAssertions=" + 0 + ", assertions=" + Collections.<TestAssertion>emptySet() + ", isCompliant=" //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
			+ false + "]"; //$NON-NLS-1$

	/**
	 * Test method for
	 * {@link org.verapdf.pdfa.results.ValidationResultImpl#hashCode()}.
	 */
	@Test
	public final void testHashCodeAndEquals() {
		EqualsVerifier.forClass(ValidationResultImpl.class).suppress(Warning.NULL_FIELDS).verify();
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
	 * {@link org.verapdf.pdfa.results.ValidationResultImpl#fromValues(org.verapdf.pdfa.flavours.PDFAFlavour, java.util.Set, boolean, int)}.
	 */
	@Test
	public final void testFromValues() {
		ValidationResult resultFromVals = ValidationResults.resultFromValues(PDFAFlavour.NO_FLAVOUR,
				Collections.<TestAssertion>emptySet(), false);
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
		Set<TestAssertion> assertions = new HashSet<>();
		assertions.add(ValidationResults.defaultAssertion());
		ValidationResult result = ValidationResults.resultFromValues(PDFAFlavour.PDFA_1_A, assertions);
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
		Set<TestAssertion> assertions = new HashSet<>();
		assertions.add(TestAssertionImpl.defaultInstance());
		ValidationResult result = ValidationResults.resultFromValues(PDFAFlavour.PDFA_1_A, assertions);
		File temp = Files.createTempFile("profile", "xml").toFile(); //$NON-NLS-1$ //$NON-NLS-2$
		try (OutputStream forXml = new FileOutputStream(temp)) {
			XmlSerialiser.toXml(result, forXml, true, true);
		}
		try (InputStream readXml = new FileInputStream(temp)) {
			ValidationResult unmarshalledResult = XmlSerialiser.typeFromXml(ValidationResultImpl.class, readXml);
			assertFalse(result == unmarshalledResult);
			assertTrue(result.equals(unmarshalledResult));
		}
	}
}
