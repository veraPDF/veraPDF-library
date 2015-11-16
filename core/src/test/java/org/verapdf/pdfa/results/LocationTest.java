/**
 * 
 */
package org.verapdf.pdfa.results;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
@SuppressWarnings("static-method")
public class LocationTest {
    private final static String DEFAULT_LOCATION_STRING = "Location [level=level, context=context]";

    /**
     * Test method for {@link org.verapdf.pdfa.results.LocationImpl#hashCode()}.
     */
    @Test
    public final void testHashCodeAndEquals() {
        EqualsVerifier.forClass(LocationImpl.class).verify();
    }

    /**
     * Test method for {@link org.verapdf.pdfa.results.LocationImpl#toString()}.
     */
    @Test
    public final void testToString() {
        assertTrue(ValidationResults.defaultLocation().toString()
                .equals(DEFAULT_LOCATION_STRING));
    }

    /**
     * Test method for {@link org.verapdf.pdfa.results.LocationImpl#defaultInstance()}.
     */
    @Test
    public final void testDefaultInstance() {
        Location defaultLoc = ValidationResults.defaultLocation();
        assertTrue(defaultLoc.equals(ValidationResults.defaultLocation()));
        assertTrue(defaultLoc == ValidationResults.defaultLocation());
    }

    /**
     * Test method for {@link org.verapdf.pdfa.results.LocationImpl#fromValues(java.lang.String, java.lang.String)}.
     */
    @Test
    public final void testFromValues() {
        Location fromValLoc = ValidationResults.locationFromValues("level", "context");
        assertTrue(fromValLoc.equals(ValidationResults.defaultLocation()));
        assertFalse(fromValLoc == ValidationResults.defaultLocation());
    }

}
