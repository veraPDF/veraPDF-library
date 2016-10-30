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

import nl.jqno.equalsverifier.EqualsVerifier;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 31 Oct 2016:20:46:07
 */

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
		MetadataFixerConfig fromVals = FixerFactory.fromValues(defaultConfig.getFixesPrefix(), defaultConfig.isFixId());
		assertTrue(defaultConfig.equals(fromVals));
		assertFalse(defaultConfig == fromVals);
	}

	/**
	 * Test method for
	 * {@link org.verapdf.metadata.fixer.FixerConfigImpl#toXml(org.verapdf.metadata.fixer.MetadataFixerConfig, java.lang.Boolean)}.
	 */
	@Test
	public void testToXmlMetadataFixerConfigBoolean() throws JAXBException, IOException {
		String defaultXml = FixerFactory.configToXml(FixerFactory.defaultConfig(), Boolean.TRUE);
		MetadataFixerConfig defaultCopy = FixerFactory.createConfig(defaultXml);
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
			FixerFactory.configToXml(FixerFactory.defaultConfig(), fos);
		}
		try (InputStream fis = new FileInputStream(temp)) {
			defaultInstance = FixerFactory.createConfig(fis);
		}
		assertTrue(defaultInstance.equals(FixerFactory.defaultConfig()));
		assertFalse(defaultInstance == FixerFactory.defaultConfig());
	}

}
