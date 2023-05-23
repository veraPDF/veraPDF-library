/**
 * This file is part of VeraPDF Library GUI, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * VeraPDF Library GUI is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with VeraPDF Library GUI as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * VeraPDF Library GUI as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
/**
 * 
 */
package org.verapdf.processor.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import javax.xml.bind.JAXBException;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

/**
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 * Created 31 Oct 2016:18:07:02
 */

public class VeraAppConfigTest {

	/**
	 * Test method for {@link VeraAppConfigImpl#defaultInstance()}.
	 */
	@Test
	public void testDefaultInstance() {
		VeraAppConfig defaultInstance = VeraAppConfigImpl.defaultInstance();
		VeraAppConfig defaultCopy = AppConfigBuilder.fromConfig(defaultInstance).build();
		assertSame(defaultInstance, VeraAppConfigImpl.defaultInstance());
		assertEquals(defaultInstance, VeraAppConfigImpl.defaultInstance());
		assertEquals(defaultCopy, defaultInstance);
		assertNotSame(defaultCopy, defaultInstance);
	}

    /**
     * Test method for {@link VeraAppConfigImpl#hashCode()}.
     */
    @SuppressWarnings("static-method")
	@Test
    public final void testHashCodeAndEquals() {
        EqualsVerifier.forClass(VeraAppConfigImpl.class).verify();
    }

	/**
	 * Test method for {@link VeraAppConfigImpl#toXml(VeraAppConfig, OutputStream, Boolean)}.
	 */
	@Test
	public void testToXmlVeraAppConfigOutputStreamBoolean() throws IOException, JAXBException {
		File temp = Files.createTempFile("", "").toFile();
		VeraAppConfig defaultInstance = VeraAppConfigImpl.defaultInstance();
		assertSame(defaultInstance, VeraAppConfigImpl.defaultInstance());
		try (OutputStream fos = new FileOutputStream(temp)) {
			VeraAppConfigImpl.toXml(VeraAppConfigImpl.defaultInstance(), fos, Boolean.TRUE);
		}
		try (InputStream fis = new FileInputStream(temp)) {
			defaultInstance = VeraAppConfigImpl.fromXml(fis);
		}
		assertEquals(defaultInstance, VeraAppConfigImpl.defaultInstance());
		assertNotSame(defaultInstance, VeraAppConfigImpl.defaultInstance());
		temp.delete();
	}

	/**
	 * Test method for {@link VeraAppConfigImpl#fromXml(String)}.
	 */
	@Test
	public void testFromXmlString() throws JAXBException, IOException {
		String defaultXml = VeraAppConfigImpl.toXml(VeraAppConfigImpl.defaultInstance(), Boolean.TRUE);
		VeraAppConfig fromXml = VeraAppConfigImpl.fromXml(defaultXml);
		assertEquals(VeraAppConfigImpl.defaultInstance(), fromXml);
		assertNotSame(fromXml, VeraAppConfigImpl.defaultInstance());
	}
}
