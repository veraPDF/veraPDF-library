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

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;
import org.verapdf.pdfa.results.TestAssertion.Status;
import org.verapdf.pdfa.validation.profiles.Profiles;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
@SuppressWarnings("static-method")
public class TestAssertionTest {
    private static final String DEFAULT_ASSERTION_STRING = "TestAssertion [ruleId=" //$NON-NLS-1$
            + Profiles.defaultRuleId()
            + ", status=" //$NON-NLS-1$
            + Status.FAILED
            + ", message=" //$NON-NLS-1$
            + "message" //$NON-NLS-1$
            + ", location=" //$NON-NLS-1$
            + ValidationResults.defaultLocation() //$NON-NLS-1$
            + ", locationContext=" //$NON-NLS-1$
            + "null" //$NON-NLS-1$
            + ", errorMessage=" //$NON-NLS-1$
            + "null" //$NON-NLS-1$
            + "]"; //$NON-NLS-1$

    /**
     * Test method for
     * {@link org.verapdf.pdfa.results.TestAssertionImpl#hashCode()}.
     */
    @Test
    public final void testHashCodeAndEquals() {
        EqualsVerifier.forClass(TestAssertionImpl.class).withIgnoredFields("ordinal", "errorArguments").verify();
    }

    /**
     * Test method for
     * {@link org.verapdf.pdfa.results.TestAssertionImpl#toString()}.
     */
    @Test
    public final void testToString() {
        assertEquals(DEFAULT_ASSERTION_STRING, ValidationResults.defaultAssertion().toString());
    }

    /**
     * Test method for
     * {@link org.verapdf.pdfa.results.TestAssertionImpl#defaultInstance()}.
     */
    @Test
    public final void testDefaultInstance() {
        TestAssertion defaultAssertion = ValidationResults.defaultAssertion();
        assertEquals(defaultAssertion, ValidationResults.defaultAssertion());
        assertSame(defaultAssertion, ValidationResults.defaultAssertion());
    }

    /**
     * Test method for
     * {@link org.verapdf.pdfa.results.TestAssertionImpl#fromValues(int, org.verapdf.pdfa.validation.RuleId, org.verapdf.pdfa.results.TestAssertion.Status, java.lang.String, org.verapdf.pdfa.results.Location)}
     * .
     */
    @Test
    public final void testFromValues() {
        TestAssertion assertionFromVals = ValidationResults
                .assertionFromValues(0, Profiles.defaultRuleId(),
                        Status.FAILED, "message", //$NON-NLS-1$
                        ValidationResults.defaultLocation(), null, null, null);
        assertEquals(assertionFromVals, ValidationResults
                .defaultAssertion());
        assertNotSame(assertionFromVals, ValidationResults.defaultAssertion());
    }

}
