/**
 * 
 */
package org.verapdf.pdfa.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBException;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;
import org.verapdf.pdfa.ValidationProfile;
import org.verapdf.pdfa.flavours.PDFAFlavour;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
@SuppressWarnings("static-method")
public class ValidationProfileImplTest {
    private final static String DEFAULT_PROFILE_STRING = "ValidationProfile [flavour=" + PDFAFlavour.NO_FLAVOUR.toString() + ", name=name, description=description, creator=creator, created=" + new Date(0) + ", hash=hash, rules=[], variables=[]]";
    /**
     * Test method for {@link org.verapdf.pdfa.validation.ValidationProfileImpl#equals(java.lang.Object)}.
     */
    @Test
    public final void testEqualsObject() {
        EqualsVerifier.forClass(ValidationProfileImpl.class).suppress(Warning.NULL_FIELDS).verify();
    }

    /**
     * Test method for {@link org.verapdf.pdfa.validation.ValidationProfileImpl#toString()}.
     */
    @Test
    public final void testToString() {
        assertTrue(ValidationProfileImpl.defaultInstance().toString()
                .equals(DEFAULT_PROFILE_STRING));
    }

    /**
     * Test method for {@link org.verapdf.pdfa.validation.ValidationProfileImpl#fromValues(org.verapdf.pdfa.flavours.PDFAFlavour, java.lang.String, java.lang.String, java.lang.String, java.util.Date, java.lang.String, java.util.Set, java.util.Set)}.
     */
    @Test
    public final void testFromValues() {
        // Get an equivalent to the default instance
        ValidationProfileImpl rule = ValidationProfileImpl
                .fromValues(PDFAFlavour.NO_FLAVOUR, "name", "description", "creator",
                        new Date(0L), "hash", Collections.EMPTY_SET,
                        Collections.EMPTY_SET);
        ValidationProfile defaultInstance = ValidationProfileImpl.defaultInstance();
        // Equivalent is NOT the same object as default instance
        assertFalse(rule == defaultInstance);
        // But it is equal
        assertTrue(rule.equals(defaultInstance));
    }

    /**
     * Test method for {@link org.verapdf.pdfa.validation.ValidationProfileImpl#fromValidationProfile(org.verapdf.pdfa.ValidationProfile)}.
     */
    @Test
    public final void testFromValidationProfile() {
        // Get an equivalent to the default instance
        ValidationProfile profile = ValidationProfileImpl.fromValidationProfile(ValidationProfileImpl.defaultInstance());
        ValidationProfile defaultInstance = ValidationProfileImpl.defaultInstance();
        // Equivalent is NOT the same object as default instance
        assertFalse(profile == defaultInstance);
        // But it is equal
        assertTrue(profile.equals(defaultInstance));
    }

    /**
     * Test method for {@link org.verapdf.pdfa.validation.ValidationProfileImpl#toXml(ValidationProfile, Boolean)}.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public final void testToXmlString() throws JAXBException, IOException {
        Set<Rule> rules = new HashSet<>();
        Set<Variable> vars = new HashSet<>();
        rules.add(RuleImpl.defaultInstance());
        vars.add(VariableImpl.defaultInstance());
        ValidationProfile profile = ValidationProfileImpl.fromValues(PDFAFlavour.NO_FLAVOUR, "name", "description", "creator",
                new Date(0L), "hash", rules, vars);
        String xmlDefault = ValidationProfileImpl.toXml(profile, Boolean.FALSE);
        ValidationProfile unmarshalledDefault = ValidationProfileImpl.fromXml(xmlDefault);
        assertFalse(profile == unmarshalledDefault);
        assertTrue(profile.equals(unmarshalledDefault));
    }

    /**
     * Test method for {@link org.verapdf.pdfa.validation.ValidationProfileImpl#toXml(ValidationProfile, OutputStream, Boolean)}.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public final void testToXmlStream() throws JAXBException, IOException {
        Set<Rule> rules = new HashSet<>();
        Set<Variable> vars = new HashSet<>();
        rules.add(RuleImpl.defaultInstance());
        vars.add(VariableImpl.defaultInstance());
        ValidationProfile profile = ValidationProfileImpl.fromValues(PDFAFlavour.NO_FLAVOUR, "name", "description", "creator",
                new Date(0L), "hash", rules, vars);
        File temp = Files.createTempFile("profile", "xml").toFile();
        try (OutputStream forXml = new FileOutputStream(temp)) {
            ValidationProfileImpl.toXml(profile, forXml, Boolean.TRUE);
        }
        try (InputStream readXml = new FileInputStream(temp)) {
            ValidationProfile unmarshalledDefault = ValidationProfileImpl.fromXml(readXml);
            assertFalse(profile == unmarshalledDefault);
            assertTrue(profile.equals(unmarshalledDefault));
        }
    }

}
