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
import org.verapdf.xmp.XMPMetaFactory;
import org.verapdf.xmp.XMPSchemaRegistry;
import org.verapdf.xmp.impl.VeraPDFMeta;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class VeraPDFMetaPrefixTest {

    private static final String XMP_PACKET =
            "<x:xmpmeta xmlns:x=\"adobe:ns:meta/\">" +
                    "<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\">" +
                    "<rdf:Description rdf:about=\"\" xmlns:custom=\"" + XMPConst.NS_PDFUA_ID + "\">" +
                    "<custom:part>1</custom:part><custom:rev>2024</custom:rev>" +
                    "</rdf:Description></rdf:RDF></x:xmpmeta>";

    @Test
    public void setPropertyPrefixUpdatesNamespaceAndSerializedMetadata() throws Exception {
        XMPSchemaRegistry registry = XMPMetaFactory.getSchemaRegistry();
        String originalPrefix = registry.getNamespacePrefix(XMPConst.NS_PDFUA_ID);
        try {
            VeraPDFMeta metadata = VeraPDFMeta.parse(new ByteArrayInputStream(
                    XMP_PACKET.getBytes(StandardCharsets.UTF_8)));

            metadata.setPropertyPrefix(XMPConst.NS_PDFUA_ID, "part", "pdfuaid");

            assertEquals("pdfuaid", metadata.getProperty(XMPConst.NS_PDFUA_ID, "part").getPrefix());
            assertEquals("pdfuaid", metadata.getProperty(XMPConst.NS_PDFUA_ID, "rev").getPrefix());
            assertEquals(Integer.valueOf(1), metadata.getPDFUAIdentificationPart());
            assertEquals("2024", metadata.getPDFUARevisionYear());

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            VeraPDFMeta.serialize(metadata, output);
            String serialized = new String(output.toByteArray(), StandardCharsets.UTF_8);
            assertTrue(serialized.contains("xmlns:pdfuaid=\"" + XMPConst.NS_PDFUA_ID + "\""));
            assertTrue(serialized.contains("pdfuaid:part"));

            VeraPDFMeta reparsed = VeraPDFMeta.parse(new ByteArrayInputStream(output.toByteArray()));
            assertEquals(Integer.valueOf(1), reparsed.getPDFUAIdentificationPart());
            assertEquals("pdfuaid", reparsed.getProperty(XMPConst.NS_PDFUA_ID, "part").getPrefix());
        } finally {
            registry.deleteNamespace(XMPConst.NS_PDFUA_ID);
            if (originalPrefix != null) {
                registry.registerNamespace(XMPConst.NS_PDFUA_ID, originalPrefix, false);
            }
        }
    }
}
