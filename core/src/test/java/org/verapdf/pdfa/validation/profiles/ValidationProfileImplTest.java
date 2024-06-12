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

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import jakarta.xml.bind.JAXBException;
import java.io.*;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
@SuppressWarnings("static-method")
public class ValidationProfileImplTest {
	private static final String DEFAULT_PROFILE_STRING = "ValidationProfile [flavour="
			+ PDFAFlavour.NO_FLAVOUR + ", details=" + ProfileDetailsImpl.defaultInstance()
			+ ", hash=hash, rules=[], variables=[]]";

	/**
	 * Test method for
	 * {@link org.verapdf.pdfa.validation.profiles.ValidationProfileImpl#equals(java.lang.Object)}
	 * .
	 */
	@Test
	public final void testEqualsObject() {
		EqualsVerifier.forClass(ValidationProfileImpl.class).withIgnoredFields("ruleLookup", "objectVariableMap", "objectRuleMap").suppress(Warning.NULL_FIELDS).verify();
	}

	/**
	 * Test method for
	 * {@link org.verapdf.pdfa.validation.profiles.ValidationProfileImpl#toString()}.
	 */
	@Test
	public final void testToString() {
		assertEquals(ValidationProfileImpl.defaultInstance().toString(), DEFAULT_PROFILE_STRING);
	}

	/**
	 * Test method for
	 * {@link org.verapdf.pdfa.validation.profiles.ValidationProfileImpl#fromValues(PDFAFlavour, ProfileDetails, String, Set, Set)}
	 * .
	 */
	@Test
	public final void testFromValues() {
		// Get an equivalent to the default instance
		ValidationProfile rule = Profiles.profileFromValues(PDFAFlavour.NO_FLAVOUR,
				ProfileDetailsImpl.defaultInstance(), "hash", Collections.emptySet(),
				Collections.emptySet());
		ValidationProfile defaultInstance = Profiles.defaultProfile();
		// Equivalent is NOT the same object as default instance
		assertNotSame(rule, defaultInstance);
		// But it is equal
		assertEquals(rule, defaultInstance);
	}

	/**
	 * Test method for
	 * {@link org.verapdf.pdfa.validation.profiles.ValidationProfileImpl#fromValues(PDFAFlavour, ProfileDetails, String, Set, Set)}
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
		assertNotSame(profile, defaultInstance);
		// But it is equal
		assertEquals(profile, defaultInstance);
	}


	/**
	 * Test method for
	 * {@link Profiles#profileToXml(ValidationProfile, OutputStream, boolean, boolean)}
	 * .
	 * 
	 * @throws JAXBException
	 * @throws IOException
	 */
	@Test
	public final void testToXmlStream() throws JAXBException, IOException {
		Set<Rule> rules = new HashSet<>();
		rules.add(Profiles.defaultRule());
		Set<Variable> vars = new HashSet<>();
		vars.add(Profiles.defaultVariable());
		ValidationProfile profile = Profiles.profileFromValues(PDFAFlavour.NO_FLAVOUR,
				ProfileDetailsImpl.defaultInstance(), "hash", rules, vars);
		File temp = Files.createTempFile("profile", "xml").toFile();
		try (OutputStream forXml = new FileOutputStream(temp)) {
			Profiles.profileToXml(profile, forXml, false, true);
		}
		try (InputStream readXml = new FileInputStream(temp)) {
			ValidationProfile unmarshalledDefault = Profiles.profileFromXml(readXml);
			assertNotSame(profile, unmarshalledDefault);
			assertEquals(profile, unmarshalledDefault);
		}
		temp.delete();
	}

}
