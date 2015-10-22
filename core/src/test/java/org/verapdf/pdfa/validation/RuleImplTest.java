/**
 * 
 */
package org.verapdf.pdfa.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBException;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
@SuppressWarnings("static-method")
public class RuleImplTest {
    private static final String DEFAULT_RULE_STRING = "Rule [id="
            + RuleIdImpl.defaultInstance()
            + ", object=object, name=name, description=description, test=test, references=[]]";

    /**
     * Test method for
     * {@link org.verapdf.pdfa.validation.RuleImpl#equals(java.lang.Object)}.
     */
    @Test
    public final void testEqualsObject() {
        EqualsVerifier.forClass(RuleImpl.class).suppress(Warning.NULL_FIELDS).verify();
    }

    /**
     * Test method for {@link org.verapdf.pdfa.validation.RuleImpl#toString()}.
     */
    @Test
    public final void testToString() {
        assertTrue(RuleImpl.defaultInstance().toString()
                .equals(DEFAULT_RULE_STRING));
    }

    /**
     * Test method for
     * {@link org.verapdf.pdfa.validation.RuleImpl#fromValues(org.verapdf.pdfa.validation.RuleIdImpl, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.List)}
     * .
     */
    @Test
    public final void testFromValues() {
        // Get an equivalent to the default instance
        RuleImpl rule = RuleImpl
                .fromValues(RuleIdImpl.defaultInstance(), "object", "name",
                        "description", "test", Collections.EMPTY_LIST);
        Rule defaultInstance = RuleImpl.defaultInstance();
        // Equivalent is NOT the same object as default instance
        assertFalse(rule == defaultInstance);
        // But it is equal
        assertTrue(rule.equals(defaultInstance));
    }

    /**
     * Test method for
     * {@link org.verapdf.pdfa.validation.RuleImpl#fromRule(org.verapdf.pdfa.validation.Rule)}
     * .
     */
    @Test
    public final void testFromRule() {
        // Get an equivalent to the default instance
        Rule rule = RuleImpl.fromRule(RuleImpl.defaultInstance());
        Rule defaultInstance = RuleImpl.defaultInstance();
        // Equivalent is NOT the same object as default instance
        assertFalse(rule == defaultInstance);
        // But it is equal
        assertTrue(rule.equals(defaultInstance));
    }

    /**
     * Test method for
     * {@link org.verapdf.pdfa.validation.RuleImpl#toXml(org.verapdf.pdfa.validation.Rule)}
     * .
     * 
     * @throws JAXBException
     */
    @Test
    public final void testToXml() throws JAXBException {
        List<Reference> refs = new ArrayList<>();
        refs.add(ReferenceImpl.defaultInstance());
        Rule rule = RuleImpl.fromValues(RuleIdImpl.defaultInstance(), "object",
                "name", "description", "test", refs);
        String xmlDefault = RuleImpl.toXml(rule);
        Rule unmarshalledDefault = RuleImpl.fromXml(xmlDefault);
        assertFalse(rule == unmarshalledDefault);
        assertTrue(rule.equals(unmarshalledDefault));
    }

}
