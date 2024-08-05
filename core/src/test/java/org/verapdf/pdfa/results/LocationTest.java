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
package org.verapdf.pdfa.results;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
@SuppressWarnings("static-method")
public class LocationTest {
    private static final String DEFAULT_LOCATION_STRING = "Location [level=level, context=context]"; //$NON-NLS-1$

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
        assertEquals(DEFAULT_LOCATION_STRING, ValidationResults.defaultLocation().toString());
    }

    /**
     * Test method for {@link org.verapdf.pdfa.results.LocationImpl#defaultInstance()}.
     */
    @Test
    public final void testDefaultInstance() {
        Location defaultLoc = ValidationResults.defaultLocation();
        assertEquals(defaultLoc, ValidationResults.defaultLocation());
        assertSame(defaultLoc, ValidationResults.defaultLocation());
    }

    /**
     * Test method for {@link org.verapdf.pdfa.results.LocationImpl#fromValues(java.lang.String, java.lang.String)}.
     */
    @Test
    public final void testFromValues() {
        Location fromValLoc = ValidationResults.locationFromValues("level", "context"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(fromValLoc, ValidationResults.defaultLocation());
        assertNotSame(fromValLoc, ValidationResults.defaultLocation());
    }

}
