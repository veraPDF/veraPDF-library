/**
 *
 */
package org.verapdf.pdfa.validation.profiles;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBException;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;
import org.verapdf.core.XmlSerialiser;
import org.verapdf.pdfa.validation.profiles.ErrorDetailsImpl;
import org.verapdf.pdfa.validation.profiles.Profiles;
import org.verapdf.pdfa.validation.profiles.Reference;
import org.verapdf.pdfa.validation.profiles.ReferenceImpl;
import org.verapdf.pdfa.validation.profiles.Rule;
import org.verapdf.pdfa.validation.profiles.RuleIdImpl;
import org.verapdf.pdfa.validation.profiles.RuleImpl;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
@SuppressWarnings("static-method")
public class RuleImplTest {
    private static final String DEFAULT_RULE_STRING = "Rule [id="
            + Profiles.defaultRuleId()
            + ", object=object, description=description, test=test, error=" + ErrorDetailsImpl.defaultInstance() + ", references=[]]";

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
        assertTrue(Profiles.defaultRule().toString()
                .equals(DEFAULT_RULE_STRING));
    }

    /**
     * Test method for
     * {@link org.verapdf.pdfa.validation.RuleImpl#fromValues(RuleId, String, String, String, ErrorDetails, List)}
     * .
     */
    @Test
    public final void testFromValues() {
        // Get an equivalent to the default instance
        RuleImpl rule = RuleImpl
                .fromValues(Profiles.defaultRuleId(), "object",
                        "description", "test", ErrorDetailsImpl.defaultInstance(), Collections.<Reference> emptyList());
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
     * {@link org.verapdf.pdfa.validation.RuleImpl#toXml(org.verapdf.pdfa.validation.Rule, Boolean)}
     * .
     *
     * @throws JAXBException
     * @throws IOException
     */
    @Test
    public final void testToXmlString() throws JAXBException {
        List<Reference> refs = new ArrayList<>();
        refs.add(ReferenceImpl.defaultInstance());
        Rule rule = RuleImpl.fromValues(RuleIdImpl.defaultInstance(), "object",
                "description", "test", ErrorDetailsImpl.defaultInstance(), refs);
        String xmlDefault = XmlSerialiser.toXml(rule, true, true);
        Rule unmarshalledDefault = XmlSerialiser.typeFromXml(RuleImpl.class, xmlDefault);
        assertFalse(rule == unmarshalledDefault);
        assertTrue(rule.equals(unmarshalledDefault));
    }

    /**
     * Test method for
     * {@link org.verapdf.pdfa.validation.RuleImpl#toXml(Rule, java.io.OutputStream, Boolean)}
     * .
     *
     * @throws JAXBException
     * @throws IOException
     */
    @Test
    public final void testToXmlStream() throws JAXBException, IOException {
        Rule rule = RuleImpl.defaultInstance();
        File temp = Files.createTempFile("profile", "xml").toFile();
        try (OutputStream forXml = new FileOutputStream(temp)) {
            XmlSerialiser.toXml(rule, forXml, true, true);
        }
        try (InputStream readXml = new FileInputStream(temp)) {
            Rule unmarshalledDefault = XmlSerialiser.typeFromXml(RuleImpl.class, readXml);
            assertFalse(rule == unmarshalledDefault);
            assertTrue(rule.equals(unmarshalledDefault));
        }
    }

}
