/**
 * 
 */
package org.verapdf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;


/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
@SuppressWarnings("static-method")
public class ReleaseDetailsTest {

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
        ReleaseDetails instance = ReleaseDetails.getInstance();
        assertTrue("0.0.0-TEST".equals(instance.getVersion()));
    }

    /**
     * Test method for {@link org.verapdf.ReleaseDetails#getBuildDate()}.
     * @throws ParseException 
     */
    @Test
    public final void testGetBuildDate() throws ParseException {
        ReleaseDetails instance = ReleaseDetails.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse("2011-07-31");
        assertTrue(instance.getBuildDate().equals(date));
    }

    /**
     * Test method for {@link org.verapdf.ReleaseDetails#toString()}.
     */
    @Test
    public final void testToString() {
        ReleaseDetails instance = ReleaseDetails.getInstance();
        assertEquals("ReleaseDetails [version=0.0.0-TEST, buildDate=Sun Jul 31 00:00:00 " + TimeZone.getDefault().getDisplayName(true, TimeZone.SHORT) + " 2011]", instance.toString());
    }

    /**
     * Test method for {@link org.verapdf.ReleaseDetails#getInstance()}.
     */
    @Test
    public final void testGetInstance() {
        ReleaseDetails instance = ReleaseDetails.getInstance();
        ReleaseDetails secondInstance = ReleaseDetails.getInstance();
        assertTrue(instance == secondInstance);
    }

}
