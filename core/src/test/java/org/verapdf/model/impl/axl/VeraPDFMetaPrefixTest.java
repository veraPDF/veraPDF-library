/*
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2026, veraPDF Consortium <info@verapdf.org>
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
package org.verapdf.model.impl.axl;

import org.junit.Test;
import org.verapdf.xmp.XMPConst;
import org.verapdf.xmp.XMPException;
import org.verapdf.xmp.XMPMetaFactory;
import org.verapdf.xmp.XMPSchemaRegistry;
import org.verapdf.xmp.impl.VeraPDFMeta;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class VeraPDFMetaPrefixTest {

    private static final String XMP_PACKET =
            "<x:xmpmeta xmlns:x=\"adobe:ns:meta/\">" +
                    "<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\">" +
                    "<rdf:Description rdf:about=\"\" xmlns:custom=\"" + XMPConst.NS_PDFUA_ID + "\">" +
                    "<custom:part>1</custom:part><custom:rev>2024</custom:rev>" +
                    "</rdf:Description></rdf:RDF></x:xmpmeta>";

    @Test
    public void setPropertyPrefixUpdatesNamespaceAndSerializedMetadata() throws Exception {
        VeraPDFMeta metadata = VeraPDFMeta.parse(new ByteArrayInputStream(
                XMP_PACKET.getBytes(StandardCharsets.UTF_8)));

        metadata.setPropertyPrefix(XMPConst.NS_PDFUA_ID, "part", "pdfuaidLocal");

        assertEquals("pdfuaidLocal", metadata.getProperty(XMPConst.NS_PDFUA_ID, "part").getPrefix());
        assertEquals("pdfuaidLocal", metadata.getProperty(XMPConst.NS_PDFUA_ID, "rev").getPrefix());
        assertEquals(Integer.valueOf(1), metadata.getPDFUAIdentificationPart());
        assertEquals("2024", metadata.getPDFUARevisionYear());

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        VeraPDFMeta.serialize(metadata, output);
        String serialized = new String(output.toByteArray(), StandardCharsets.UTF_8);
        assertTrue(serialized.contains("xmlns:pdfuaidLocal=\"" + XMPConst.NS_PDFUA_ID + "\""));
        assertTrue(serialized.contains("pdfuaidLocal:part"));

        VeraPDFMeta reparsed = VeraPDFMeta.parse(new ByteArrayInputStream(output.toByteArray()));
        assertEquals(Integer.valueOf(1), reparsed.getPDFUAIdentificationPart());
        assertEquals("pdfuaidLocal", reparsed.getProperty(XMPConst.NS_PDFUA_ID, "part").getPrefix());
    }

    @Test
    public void setPdfaPropertyPrefixUpdatesNamespaceAndSerializedMetadata() throws Exception {
        String packet = "<x:xmpmeta xmlns:x=\"adobe:ns:meta/\">" +
                "<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\">" +
                "<rdf:Description rdf:about=\"\" xmlns:custom=\"" + XMPConst.NS_PDFA_ID + "\">" +
                "<custom:part>1</custom:part><custom:conformance>B</custom:conformance>" +
                "</rdf:Description></rdf:RDF></x:xmpmeta>";
        VeraPDFMeta metadata = VeraPDFMeta.parse(new ByteArrayInputStream(
                packet.getBytes(StandardCharsets.UTF_8)));

        metadata.setPropertyPrefix(XMPConst.NS_PDFA_ID, "part", "pdfaid");

        assertEquals("pdfaid", metadata.getProperty(XMPConst.NS_PDFA_ID, "part").getPrefix());
        assertEquals("pdfaid", metadata.getProperty(XMPConst.NS_PDFA_ID, "conformance").getPrefix());
        assertEquals(Integer.valueOf(1), metadata.getPDFAIdentificationPart());
        assertEquals("B", metadata.getPDFAIdentificationConformance());

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        VeraPDFMeta.serialize(metadata, output);
        String serialized = new String(output.toByteArray(), StandardCharsets.UTF_8);
        assertTrue(serialized.contains("xmlns:pdfaid=\"" + XMPConst.NS_PDFA_ID + "\""));
        assertTrue(serialized.contains("pdfaid:part"));

        VeraPDFMeta reparsed = VeraPDFMeta.parse(new ByteArrayInputStream(output.toByteArray()));
        assertEquals(Integer.valueOf(1), reparsed.getPDFAIdentificationPart());
        assertEquals("pdfaid", reparsed.getProperty(XMPConst.NS_PDFA_ID, "part").getPrefix());
    }

    @Test
    public void changingPrefixDoesNotAffectOtherMetadataOrSharedRegistry() throws Exception {
        XMPSchemaRegistry registry = XMPMetaFactory.getSchemaRegistry();
        String originalPrefix = registry.getNamespacePrefix(XMPConst.NS_PDFUA_ID);
        String originalNamespace = registry.getNamespaceURI(originalPrefix);
        VeraPDFMeta changed = VeraPDFMeta.parse(new ByteArrayInputStream(
                XMP_PACKET.getBytes(StandardCharsets.UTF_8)));
        VeraPDFMeta unchanged = VeraPDFMeta.parse(new ByteArrayInputStream(
                XMP_PACKET.getBytes(StandardCharsets.UTF_8)));

        changed.setPropertyPrefix(XMPConst.NS_PDFUA_ID, "part", "uaLocal");

        assertEquals(Integer.valueOf(1), changed.getPDFUAIdentificationPart());
        assertEquals(Integer.valueOf(1), unchanged.getPDFUAIdentificationPart());
        assertEquals("2024", unchanged.getPDFUARevisionYear());
        assertEquals(originalPrefix, registry.getNamespacePrefix(XMPConst.NS_PDFUA_ID));
        assertEquals(originalNamespace, registry.getNamespaceURI(originalPrefix));
    }

    @Test
    public void setPropertyPrefixRejectsPropertyRemovedFromLiveTree() throws Exception {
        VeraPDFMeta metadata = VeraPDFMeta.parse(new ByteArrayInputStream(
                XMP_PACKET.getBytes(StandardCharsets.UTF_8)));
        metadata.setPDFUAIdentificationPart(null);

        try {
            metadata.setPropertyPrefix(XMPConst.NS_PDFUA_ID, "part", "uaLocal");
            fail("Expected a missing-property exception");
        } catch (XMPException e) {
            assertEquals(org.verapdf.xmp.XMPError.BADXPATH, e.getErrorCode());
        }
    }

    @Test
    public void setPropertyPrefixSupportsPdfaExtensionSchemasProperty() throws Exception {
        VeraPDFMeta metadata = VeraPDFMeta.create();
        metadata.createExtensionSchema();

        metadata.setPropertyPrefix(XMPConst.NS_PDFA_EXTENSION, "schemas", "extensionLocal");

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        VeraPDFMeta.serialize(metadata, output);
        String serialized = new String(output.toByteArray(), StandardCharsets.UTF_8);
        assertTrue(serialized.contains("xmlns:extensionLocal=\"" + XMPConst.NS_PDFA_EXTENSION + "\""));
        assertTrue(serialized.contains("extensionLocal:schemas"));
    }

    @Test
    public void serializationRenamesNestedFieldsAndQualifiersWithoutChangingOtherNamespaces() throws Exception {
        String packet = "<x:xmpmeta xmlns:x=\"adobe:ns:meta/\">" +
                "<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\">" +
                "<rdf:Description rdf:about=\"\" xmlns:custom=\"" + XMPConst.NS_PDFUA_ID + "\"" +
                " xmlns:pdf=\"" + XMPConst.NS_PDF + "\">" +
                "<custom:details rdf:parseType=\"Resource\">" +
                "<custom:nested rdf:parseType=\"Resource\"><custom:field>value</custom:field>" +
                "<pdf:field>other</pdf:field></custom:nested></custom:details>" +
                "<custom:qualified rdf:parseType=\"Resource\"><rdf:value>text</rdf:value>" +
                "<custom:qualifier>value</custom:qualifier></custom:qualified>" +
                "</rdf:Description></rdf:RDF></x:xmpmeta>";
        VeraPDFMeta metadata = VeraPDFMeta.parse(new ByteArrayInputStream(
                packet.getBytes(StandardCharsets.UTF_8)));

        metadata.setPropertyPrefix(XMPConst.NS_PDFUA_ID, "details", "uaLocal");

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        VeraPDFMeta.serialize(metadata, output);
        String serialized = new String(output.toByteArray(), StandardCharsets.UTF_8);
        assertTrue(serialized.contains("uaLocal:nested"));
        assertTrue(serialized.contains("uaLocal:field"));
        assertTrue(serialized.contains("uaLocal:qualifier"));
        assertTrue(serialized.contains("pdf:field"));
        assertFalse(serialized.contains("=\"null\""));
        VeraPDFMeta reparsed = VeraPDFMeta.parse(new ByteArrayInputStream(output.toByteArray()));
        assertEquals("uaLocal", reparsed.getProperty(XMPConst.NS_PDFUA_ID, "details").getPrefix());
    }
}
