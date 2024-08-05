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
package org.verapdf.metadata.fixer;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;
import org.verapdf.core.XmlSerialiser;

import jakarta.xml.bind.JAXBException;
import java.io.*;
import java.nio.file.Files;

import static org.junit.Assert.*;

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
		assertSame(defaultConfig, FixerFactory.defaultConfig());
		assertEquals(defaultConfig, FixerFactory.defaultConfig());
	}

	/**
	 * Test method for
	 * {@link org.verapdf.metadata.fixer.FixerFactory#configFromValues(String)}
	 */
	@Test
	public void testFromValues() {
		MetadataFixerConfig defaultConfig = FixerFactory.defaultConfig();
		MetadataFixerConfig fromVals = FixerFactory.configFromValues(defaultConfig.getFixesPrefix());
		assertEquals(defaultConfig, fromVals);
		assertNotSame(defaultConfig, fromVals);
	}

	/**
	 * Test method for
	 * {@link XmlSerialiser#toXml(Object, OutputStream, boolean, boolean)}
	 */
	@Test
	public void testToXmlMetadataFixerConfigBoolean() throws JAXBException {
		String defaultXml = XmlSerialiser.toXml(FixerFactory.defaultConfig(), true, true);
		MetadataFixerConfig defaultCopy = XmlSerialiser.typeFromXml(FixerConfigImpl.class, defaultXml);
		assertEquals(defaultCopy, FixerFactory.defaultConfig());
		assertNotSame(defaultCopy, FixerFactory.defaultConfig());
	}

	/**
	 * Test method for
	 * {@link XmlSerialiser#toXml(Object, OutputStream, boolean, boolean)}.
	 */
	@Test
	public void testToXmlMetadataFixerConfigOutputStreamBoolean() throws IOException, JAXBException {
		File temp = Files.createTempFile("", "").toFile();
		MetadataFixerConfig defaultInstance = FixerFactory.defaultConfig();
		assertSame(defaultInstance, FixerFactory.defaultConfig());
		try (OutputStream fos = new FileOutputStream(temp)) {
			XmlSerialiser.toXml(FixerFactory.defaultConfig(), fos, true, true);
		}
		try (InputStream fis = new FileInputStream(temp)) {
			defaultInstance = XmlSerialiser.typeFromXml(FixerConfigImpl.class, fis);
		}
		assertEquals(defaultInstance, FixerFactory.defaultConfig());
		assertNotSame(defaultInstance, FixerFactory.defaultConfig());
		temp.delete();
	}

}
