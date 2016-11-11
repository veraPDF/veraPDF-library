/**
 * 
 */
package org.verapdf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.JAXBException;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.core.XmlSerialiser;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
@SuppressWarnings("static-method")
public class ReleaseDetailsTest {
	private static String NAME = "verapdf-test";

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
		assertTrue("0.0.0-TEST".equals(instance.getVersion()));
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
		assertTrue(instance.getBuildDate().equals(date));
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
		assertTrue(instance == secondInstance);
	}

	/**
     * Test method for {@link org.verapdf.ReleaseDetails#toXml(ReleaseDetails, OutputStream, Boolean)} and {@link org.verapdf.ReleaseDetails#fromXml(InputStream)}.
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
            assertFalse(details == unmarshalledResult);
            assertTrue(details.equals(unmarshalledResult));
        }
    }
}
