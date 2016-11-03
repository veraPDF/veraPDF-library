/**
 * 
 */
package org.verapdf.pdfa.validation.profiles;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.xml.bind.JAXBException;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;
import org.verapdf.core.XmlSerialiser;
import org.verapdf.pdfa.validation.profiles.Profiles;
import org.verapdf.pdfa.validation.profiles.Variable;
import org.verapdf.pdfa.validation.profiles.VariableImpl;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
@SuppressWarnings("static-method")
public class VariableImplTest {
    private final static String DEFAULT_VARIABLE_STRING = "Variable [name=name, object=object, defaultValue=defaultValue, value=value]";

    /**
     * Test method for
     * {@link org.verapdf.pdfa.validation.VariableImpl#equals(java.lang.Object)}
     * .
     */
    @Test
    public final void testEqualsObject() {
        EqualsVerifier.forClass(VariableImpl.class).verify();
    }

    /**
     * Test method for
     * {@link org.verapdf.pdfa.validation.VariableImpl#toString()}.
     */
    @Test
    public final void testToString() {
        assertTrue(Profiles.defaultVariable().toString()
                .equals(DEFAULT_VARIABLE_STRING));
    }

    /**
     * Test method for
     * {@link org.verapdf.pdfa.validation.VariableImpl#fromValues(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * .
     * 
     * @throws JAXBException
     */
    @Test
    public final void testFromValues() {
        // Get an equivalent to the default instance
        Variable variable = Profiles.variableFromValues("name", "object",
                "defaultValue", "value");
        Variable defaultInstance = Profiles.defaultVariable();
        // Equivalent is NOT the same object as default instance
        assertFalse(variable == defaultInstance);
        // But it is equal
        assertTrue(variable.equals(defaultInstance));
    }


    /**
     * Test method for
     * {@link org.verapdf.pdfa.validation.VariableImpl#fromVariable(Variable)}
     * .
     * 
     * @throws JAXBException
     */
    @Test
    public final void testFromVariable() {
        // Get an equivalent to the default instance
        Variable variable = VariableImpl.fromVariable(VariableImpl.defaultInstance());
        Variable defaultInstance = Profiles.defaultVariable();
        // Equivalent is NOT the same object as default instance
        assertFalse(variable == defaultInstance);
        // But it is equal
        assertTrue(variable.equals(defaultInstance));
    }

    /**
     * Test method for
     * {@link org.verapdf.pdfa.validation.VariableImpl#toXml(Variable)} and
     * {@link org.verapdf.pdfa.validation.VariableImpl#fromXml(String)}.
     * 
     * @throws JAXBException
     */
    @Test
    public final void testXmlMarshalling() throws JAXBException {
        Variable defaultInstance = Profiles.defaultVariable();
        String xmlDefault = XmlSerialiser.toXml(defaultInstance, false, false);
        Variable unmarshalledDefault = XmlSerialiser.typeFromXml(VariableImpl.class, xmlDefault);
        assertFalse(defaultInstance == unmarshalledDefault);
        assertTrue(defaultInstance == VariableImpl.defaultInstance());
        assertTrue(defaultInstance.equals(unmarshalledDefault));
    }

}
