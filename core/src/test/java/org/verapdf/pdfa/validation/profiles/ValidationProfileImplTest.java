/**
 *
 */
package org.verapdf.pdfa.validation.profiles;

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
import org.verapdf.pdfa.flavours.PDFAFlavour;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
@SuppressWarnings("static-method")
public class ValidationProfileImplTest {
	private final static String DEFAULT_PROFILE_STRING = "ValidationProfile [flavour="
			+ PDFAFlavour.NO_FLAVOUR.toString() + ", details=" + ProfileDetailsImpl.defaultInstance()
			+ ", hash=hash, rules=[], variables=[]]";

	/**
	 * Test method for
	 * {@link org.verapdf.pdfa.validation.ValidationProfileImpl#equals(java.lang.Object)}
	 * .
	 */
	@Test
	public final void testEqualsObject() {
		EqualsVerifier.forClass(ValidationProfileImpl.class).suppress(Warning.NULL_FIELDS).verify();
	}

	/**
	 * Test method for
	 * {@link org.verapdf.pdfa.validation.ValidationProfileImpl#toString()}.
	 */
	@Test
	public final void testToString() {
		assertTrue(ValidationProfileImpl.defaultInstance().toString().equals(DEFAULT_PROFILE_STRING));
	}

	/**
	 * Test method for
	 * {@link org.verapdf.pdfa.validation.ValidationProfileImpl#fromValues(PDFAFlavour, ProfileDetails, String, Set, Set)}
	 * .
	 */
	@Test
	public final void testFromValues() {
		// Get an equivalent to the default instance
		ValidationProfile rule = Profiles.profileFromValues(PDFAFlavour.NO_FLAVOUR,
				ProfileDetailsImpl.defaultInstance(), "hash", Collections.<Rule>emptySet(),
				Collections.<Variable>emptySet());
		ValidationProfile defaultInstance = Profiles.defaultProfile();
		// Equivalent is NOT the same object as default instance
		assertFalse(rule == defaultInstance);
		// But it is equal
		assertTrue(rule.equals(defaultInstance));
	}

	/**
	 * Test method for
	 * {@link org.verapdf.pdfa.validation.ValidationProfileImpl#fromValidationProfile(org.verapdf.pdfa.validation.ValidationProfile)}
	 * .
	 */
	@Test
	public final void testFromValidationProfile() {
		ValidationProfile defaultInstance = Profiles.defaultProfile();
		// Get an equivalent to the default instance
		ValidationProfile profile = ValidationProfileImpl.fromValues(defaultInstance.getPDFAFlavour(),
				defaultInstance.getDetails(), defaultInstance.getHexSha1Digest(), defaultInstance.getRules(),
				defaultInstance.getVariables());
		// Equivalent is NOT the same object as default instance
		assertFalse(profile == defaultInstance);
		// But it is equal
		assertTrue(profile.equals(defaultInstance));
	}


	/**
	 * Test method for
	 * {@link org.verapdf.pdfa.validation.ValidationProfileImpl#toXml(ValidationProfile, OutputStream, Boolean)}
	 * .
	 * 
	 * @throws JAXBException
	 * @throws IOException
	 */
	@Test
	public final void testToXmlStream() throws JAXBException, IOException {
		Set<Rule> rules = new HashSet<>();
		Set<Variable> vars = new HashSet<>();
		rules.add(Profiles.defaultRule());
		vars.add(Profiles.defaultVariable());
		ValidationProfile profile = Profiles.profileFromValues(PDFAFlavour.NO_FLAVOUR,
				ProfileDetailsImpl.defaultInstance(), "hash", rules, vars);
		File temp = Files.createTempFile("profile", "xml").toFile();
		try (OutputStream forXml = new FileOutputStream(temp)) {
			Profiles.profileToXml(profile, forXml, false, true);
		}
		try (InputStream readXml = new FileInputStream(temp)) {
			ValidationProfile unmarshalledDefault = Profiles.profileFromXml(readXml);
			assertFalse(profile == unmarshalledDefault);
			assertTrue(profile.equals(unmarshalledDefault));
		}
	}

}
