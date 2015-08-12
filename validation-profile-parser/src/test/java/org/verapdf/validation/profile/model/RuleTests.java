/**
 * 
 */
package org.verapdf.validation.profile.model;

import static org.junit.Assert.*;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
@SuppressWarnings("static-method")
public class RuleTests {

    /**
     * Test method for {@link org.verapdf.validation.profile.model.Rule#Rule(java.lang.String, java.lang.String, java.lang.String, org.verapdf.validation.profile.model.RuleError, boolean, java.lang.String, org.verapdf.validation.profile.model.Reference, java.util.List)}.
     */
    @SuppressWarnings("unused")
    @Test (expected=IllegalArgumentException.class)
    public final void testRuleWithNullId() {
        new Rule(null, "", "", null, false, null, null, null);
    }

    /**
     * Test method for {@link org.verapdf.validation.profile.model.Rule#getFixes()}.
     */
    @Test
    public final void testGetFixes() {
        Rule instance = new Rule("", "", "", null, false, null, null, null);
        assertTrue(instance.getFixes() != null);
    }

    /**
     * Test method for {@link java.lang.Object#equals(java.lang.Object)} using EqualsVerifier
     */
    @Test
    public void testEqualsContract() {
        EqualsVerifier.forClass(Rule.class).verify();
    }


}
