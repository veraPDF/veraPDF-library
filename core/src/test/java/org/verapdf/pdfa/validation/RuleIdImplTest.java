/**
 * 
 */
package org.verapdf.pdfa.validation;

import static org.junit.Assert.*;

import javax.xml.bind.JAXBException;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;
import org.verapdf.pdfa.flavours.PDFAFlavour.Specification;

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

    /**
     * Test method for {@link org.verapdf.pdfa.validation.RuleIdImpl#toXml(org.verapdf.pdfa.validation.RuleId)}.
     * @throws JAXBException 
     */
    @Test
    public final void testToXml() throws JAXBException {
        RuleId defaultInstance = Profiles.defaultRuleId();
        String xmlDefault = RuleIdImpl.toXml(defaultInstance);
        RuleId unmarshalledDefault = RuleIdImpl.fromXml(xmlDefault);
        assertFalse(defaultInstance == unmarshalledDefault);
        assertTrue(defaultInstance == RuleIdImpl.defaultInstance());
        assertTrue(defaultInstance.equals(unmarshalledDefault));
    }

}
