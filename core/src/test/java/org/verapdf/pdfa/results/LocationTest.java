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
