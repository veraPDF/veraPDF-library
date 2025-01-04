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
package org.verapdf;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.core.XmlSerialiser;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
@SuppressWarnings("static-method")
public class ReleaseDetailsTest {
	private static final String NAME = "verapdf-test";

	@BeforeClass
	public static final void Before() {
		ReleaseDetails.addDetailsFromResource(
				ReleaseDetails.APPLICATION_PROPERTIES_ROOT + "test." + ReleaseDetails.PROPERTIES_EXT);
	}

	/**
	 * Test method for {@link org.verapdf.ReleaseDetails#hashCode()}.
	 */
	@Test
	public final void testHashCodeAndEquals() {
		EqualsVerifier.forClass(ReleaseDetails.class).verify();
	}

	/**
	 * Test method for {@link org.verapdf.ReleaseDetails#getVersion()}.
	 */
	@Test
	public final void testGetVersion() {
		ReleaseDetails instance = ReleaseDetails.byId(NAME);
		assertEquals("0.0.0-TEST", instance.getVersion());
	}

	/**
	 * Test method for {@link org.verapdf.ReleaseDetails#getBuildDate()}.
	 * 
	 * @throws ParseException
	 */
	@Test
	public final void testGetBuildDate() throws ParseException {
		ReleaseDetails instance = ReleaseDetails.byId(NAME);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = formatter.parse("2011-07-31");
		assertEquals(instance.getBuildDate(), date);
	}

	/**
	 * Test method for {@link org.verapdf.ReleaseDetails#toString()}.
	 * 
	 * @throws ParseException
	 */
	@Test
	public final void testToString() throws ParseException {
		ReleaseDetails instance = ReleaseDetails.byId(NAME);
		String dateFormat = "yyyy-MM-dd";
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		Date date = formatter.parse("2011-07-31");
		assertEquals("ReleaseDetails [id=" + NAME + ", version=0.0.0-TEST, buildDate=" + date.toString() + "]",
				instance.toString());
	}

	/**
	 * Test method for {@link org.verapdf.ReleaseDetails#defaultInstance()}.
	 */
	@Test
	public final void testGetDefaultInstance() {
		ReleaseDetails instance = ReleaseDetails.defaultInstance();
		ReleaseDetails secondInstance = ReleaseDetails.defaultInstance();
		assertSame(instance, secondInstance);
	}

	/**
     * Test method for {@link org.verapdf.core.XmlSerialiser#toXml(Object, OutputStream, boolean, boolean)}
     * @throws IOException 
     * @throws JAXBException 
     */
    @Test
    public final void testToAndFromXml() throws IOException, JAXBException {
        ReleaseDetails details = ReleaseDetails.byId(NAME);
        File temp = Files.createTempFile("details", "xml").toFile();
        try (OutputStream forXml = new FileOutputStream(temp)) {
        	XmlSerialiser.toXml(details, forXml, true, true);
        }
        try (InputStream readXml = new FileInputStream(temp)) {
            ReleaseDetails unmarshalledResult = XmlSerialiser
                    .typeFromXml(ReleaseDetails.class, readXml);
			assertNotSame(details, unmarshalledResult);
			assertEquals(details, unmarshalledResult);
        }
        temp.delete();
    }
}
