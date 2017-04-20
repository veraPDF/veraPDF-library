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
package org.verapdf.pdfa.validation.profiles;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.verapdf.pdfa.flavours.PDFAFlavour.Specification;

import nl.jqno.equalsverifier.EqualsVerifier;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
@SuppressWarnings("static-method")
public class RuleIdImplTest {
    private final static String DEFAULT_RULE_ID_STRING = "RuleId [specification=" + Specification.NO_STANDARD.toString() + ", clause=clause, testNumber=0]";
    /**
     * Test method for {@link org.verapdf.pdfa.validation.RuleIdImpl#equals(java.lang.Object)}.
     */
    @Test
    public final void testEqualsObject() {
        EqualsVerifier.forClass(RuleIdImpl.class).verify();
    }

    /**
     * Test method for {@link org.verapdf.pdfa.validation.RuleIdImpl#toString()}.
     */
    @Test
    public final void testToString() {
        assertTrue(Profiles.defaultRuleId().toString()
                .equals(DEFAULT_RULE_ID_STRING));
    }

    /**
     * Test method for {@link org.verapdf.pdfa.validation.RuleIdImpl#fromValues(org.verapdf.pdfa.flavours.PDFAFlavour.Specification, java.lang.String, int)}.
     */
    @Test
    public final void testFromValues() {
        // Get an equivalent to the default instance
        RuleId ruleId = Profiles.ruleIdFromValues(Specification.NO_STANDARD, "clause", 0);
        RuleId defaultInstance = Profiles.defaultRuleId();
        // Equivalent is NOT the same object as default instance
        assertFalse(ruleId == defaultInstance);
        // But it is equal
        assertTrue(ruleId.equals(defaultInstance));
    }

    /**
     * Test method for {@link org.verapdf.pdfa.validation.RuleIdImpl#fromRuleId(org.verapdf.pdfa.validation.RuleId)}.
     */
    @Test
    public final void testFromRuleId() {
        // Get an equivalent to the default instance
        RuleId ruleId = RuleIdImpl.fromRuleId(RuleIdImpl.defaultInstance());
        RuleId defaultInstance = Profiles.defaultRuleId();
        // Equivalent is NOT the same object as default instance
        assertFalse(ruleId == defaultInstance);
        // But it is equal
        assertTrue(ruleId.equals(defaultInstance));
    }
}
