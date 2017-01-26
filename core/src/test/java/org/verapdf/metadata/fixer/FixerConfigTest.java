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
package org.verapdf.metadata.fixer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.verapdf.core.XmlSerialiser;

import nl.jqno.equalsverifier.EqualsVerifier;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 31 Oct 2016:20:46:07
 */
@SuppressWarnings("static-method")
public class FixerConfigTest {

	/**
	 * Test method for
	 * {@link org.verapdf.metadata.fixer.FixerConfigImpl#hashCode()}.
	 */
	@Test
	public final void testHashCodeAndEquals() {
		EqualsVerifier.forClass(FixerConfigImpl.class).verify();
	}

	/**
	 * Test method for
	 * {@link org.verapdf.metadata.fixer.FixerConfigImpl#defaultInstance()}.
	 */
	@Test
	public void testDefaultInstance() {
		MetadataFixerConfig defaultConfig = FixerFactory.defaultConfig();
		assertTrue(defaultConfig == FixerFactory.defaultConfig());
		assertTrue(defaultConfig.equals(FixerFactory.defaultConfig()));
	}

	/**
	 * Test method for
	 * {@link org.verapdf.metadata.fixer.FixerConfigImpl#fromValues(java.lang.String, boolean)}.
	 */
	@Test
	public void testFromValues() {
		MetadataFixerConfig defaultConfig = FixerFactory.defaultConfig();
		MetadataFixerConfig fromVals = FixerFactory.configFromValues(defaultConfig.getFixesPrefix(), defaultConfig.isFixId());
		assertTrue(defaultConfig.equals(fromVals));
		assertFalse(defaultConfig == fromVals);
	}

	/**
	 * Test method for
	 * {@link org.verapdf.metadata.fixer.FixerConfigImpl#toXml(org.verapdf.metadata.fixer.MetadataFixerConfig, java.lang.Boolean)}.
	 */
	@Test
	public void testToXmlMetadataFixerConfigBoolean() throws JAXBException {
		String defaultXml = XmlSerialiser.toXml(FixerFactory.defaultConfig(), true, true);
		MetadataFixerConfig defaultCopy = XmlSerialiser.typeFromXml(FixerConfigImpl.class, defaultXml);
		assertTrue(defaultCopy.equals(FixerFactory.defaultConfig()));
		assertFalse(defaultCopy == FixerFactory.defaultConfig());
	}

	/**
	 * Test method for
	 * {@link org.verapdf.metadata.fixer.FixerConfigImpl#toXml(org.verapdf.metadata.fixer.MetadataFixerConfig, java.io.OutputStream, java.lang.Boolean)}.
	 */
	@Test
	public void testToXmlMetadataFixerConfigOutputStreamBoolean() throws IOException, JAXBException {
		File temp = Files.createTempFile("", "").toFile();
		MetadataFixerConfig defaultInstance = FixerFactory.defaultConfig();
		assertTrue(defaultInstance == FixerFactory.defaultConfig());
		try (OutputStream fos = new FileOutputStream(temp)) {
			XmlSerialiser.toXml(FixerFactory.defaultConfig(), fos, true, true);
		}
		try (InputStream fis = new FileInputStream(temp)) {
			defaultInstance = XmlSerialiser.typeFromXml(FixerConfigImpl.class, fis);
		}
		assertTrue(defaultInstance.equals(FixerFactory.defaultConfig()));
		assertFalse(defaultInstance == FixerFactory.defaultConfig());
	}

}
