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

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.verapdf.core.XmlSerialiser;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
@SuppressWarnings("static-method")
public class RuleImplTest {
    private static final String DEFAULT_RULE_STRING = "Rule [id="
            + Profiles.defaultRuleId()
            + ", object=object, deferred=null, description=description, test=test, error=" + ErrorDetailsImpl.defaultInstance() + ", references=[]]";

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
                .fromValues(Profiles.defaultRuleId(), "object", null,
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
        Rule rule = RuleImpl.fromValues(RuleIdImpl.defaultInstance(), "object", null,
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
        temp.delete();
    }

}
