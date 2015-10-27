/**
 * 
 */
package org.verapdf.pdfa.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.xml.bind.JAXBException;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
@SuppressWarnings("static-method")
public class ReferenceImplTest {
    private final static String DEFAULT_REFERENCE_STRING = "Reference [specification=specification, clause=clause]";

    /**
     * Test method for {@link org.verapdf.pdfa.validation.ReferenceImpl#equals(java.lang.Object)}.
     */
    @Test
    public final void testEqualsObject() {
        EqualsVerifier.forClass(ReferenceImpl.class).verify();
    }

    /**
     * Test method for {@link org.verapdf.pdfa.validation.ReferenceImpl#toString()}.
     */
    @Test
    public final void testToString() {
        assertTrue(ReferenceImpl.defaultInstance().toString()
                .equals(DEFAULT_REFERENCE_STRING));
    }

    /**
     * Test method for {@link org.verapdf.pdfa.validation.ReferenceImpl#fromValues(String, String)}.
     */
    @Test
    public final void testFromValues() {
        // Get an equivalent to the default instance
        ReferenceImpl reference = ReferenceImpl.fromValues("specification", "clause");
        Reference defaultInstance = ReferenceImpl.defaultInstance();
        // Equivalent is NOT the same object as default instance
        assertFalse(reference == defaultInstance);
        // But it is equal
        assertTrue(reference.equals(defaultInstance));
    }

    /**
     * Test method for {@link org.verapdf.pdfa.validation.ReferenceImpl#fromReference(org.verapdf.pdfa.validation.Reference)}.
     */
    @Test
    public final void testFromReference() {
        // Get an equivalent to the default instance
        Reference reference = ReferenceImpl.fromReference(ReferenceImpl.defaultInstance());
        Reference defaultInstance = ReferenceImpl.defaultInstance();
        // Equivalent is NOT the same object as default instance
        assertFalse(reference == defaultInstance);
        // But it is equal
        assertTrue(reference.equals(defaultInstance));
    }

    /**
     * Test method for {@link org.verapdf.pdfa.validation.ReferenceImpl#toXml(org.verapdf.pdfa.validation.Reference)}.
     * @throws JAXBException 
     */
    @Test
    public final void testToXml() throws JAXBException {
        Reference defaultInstance = ReferenceImpl.defaultInstance();
        String xmlDefault = ReferenceImpl.toXml(defaultInstance);
        Reference unmarshalledDefault = ReferenceImpl.fromXml(xmlDefault);
        assertFalse(defaultInstance == unmarshalledDefault);
        assertTrue(defaultInstance == ReferenceImpl.defaultInstance());
        assertTrue(defaultInstance.equals(unmarshalledDefault));
    }

}
