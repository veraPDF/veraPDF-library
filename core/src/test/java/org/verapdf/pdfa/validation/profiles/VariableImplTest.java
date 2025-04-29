/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2025, veraPDF Consortium <info@verapdf.org>
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

import jakarta.xml.bind.JAXBException;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;
import org.verapdf.core.XmlSerialiser;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
@SuppressWarnings("static-method")
public class VariableImplTest {
    private static final String DEFAULT_VARIABLE_STRING = "Variable [name=name, object=object, defaultValue=defaultValue, value=value]";

    /**
     * Test method for
     * {@link org.verapdf.pdfa.validation.profiles.VariableImpl#equals(java.lang.Object)}
     * .
     */
    @Test
    public final void testEqualsObject() {
        EqualsVerifier.forClass(VariableImpl.class).verify();
    }

    /**
     * Test method for
     * {@link org.verapdf.pdfa.validation.profiles.VariableImpl#toString()}.
     */
    @Test
    public final void testToString() {
        assertEquals(Profiles.defaultVariable().toString(), DEFAULT_VARIABLE_STRING);
    }

    /**
     * Test method for
     * {@link org.verapdf.pdfa.validation.profiles.VariableImpl#fromValues(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
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
        assertNotSame(variable, defaultInstance);
        // But it is equal
        assertEquals(variable, defaultInstance);
    }


    /**
     * Test method for
     * {@link org.verapdf.pdfa.validation.profiles.VariableImpl#fromVariable(Variable)}
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
        assertNotSame(variable, defaultInstance);
        // But it is equal
        assertEquals(variable, defaultInstance);
    }

    /**
     * Test method for
     * {@link XmlSerialiser#toXml(Object, boolean, boolean)} and
     * {@link XmlSerialiser#typeFromXml(Class, String)} (String)}.
     * 
     * @throws JAXBException
     */
    @Test
    public final void testXmlMarshalling() throws JAXBException {
        Variable defaultInstance = Profiles.defaultVariable();
        String xmlDefault = XmlSerialiser.toXml(defaultInstance, false, false);
        Variable unmarshalledDefault = XmlSerialiser.typeFromXml(VariableImpl.class, xmlDefault);
        assertNotSame(defaultInstance, unmarshalledDefault);
        assertSame(defaultInstance, VariableImpl.defaultInstance());
        assertEquals(defaultInstance, unmarshalledDefault);
    }

}
