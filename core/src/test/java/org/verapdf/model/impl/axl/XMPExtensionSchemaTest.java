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
package org.verapdf.model.impl.axl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.impl.VeraPDFMeta;

/**
 * @author Maksim Bezrukov
 */
@RunWith(Parameterized.class)
public class XMPExtensionSchemaTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays
                .asList(new Object[][] {
                        {
                                "org/verapdf/model/impl/axl/xmp-extension-schema-container-type-check-1.xml",
                                Boolean.TRUE, "pdfaExtension", Integer.valueOf(1)},
                        {
                                "org/verapdf/model/impl/axl/xmp-extension-schema-container-type-check-2.xml",
                                Boolean.FALSE, "ext" ,  Integer.valueOf(1)},
                        {
                                "org/verapdf/model/impl/axl/xmp-extension-schema-container-type-check-3.xml",
                                Boolean.FALSE, "pdfaExtension" ,  Integer.valueOf(1)},
                        {
                                "org/verapdf/model/impl/axl/xmp-extension-schema-container-type-check-4.xml",
                                Boolean.FALSE, "smth" ,  Integer.valueOf(0)} });
    }

    @Parameterized.Parameter
    public String filePath;

    @Parameterized.Parameter(value = 1)
    public Boolean isValidValueType;

    @Parameterized.Parameter(value = 2)
    public String prefix;

    @Parameterized.Parameter(value = 3)
    public int propertiesNumber;

    @Test
    public void test() throws URISyntaxException, XMPException, IOException {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(this.filePath)) {
            VeraPDFMeta meta = VeraPDFMeta.parse(in);
            AXLXMPPackage pack = new AXLXMPPackage(meta, true, null,
                    PDFAFlavour.PDFA_1_B);
            AXLExtensionSchemasContainer container = (AXLExtensionSchemasContainer) pack
                    .getLinkedObjects(
                            AXLXMPPackage.EXTENSION_SCHEMAS_CONTAINERS).get(0);
            assertEquals(this.isValidValueType, container.getisValidBag());
            assertEquals(this.prefix, container.getprefix());

            List<? extends org.verapdf.model.baselayer.Object> linkedObjects = container.getLinkedObjects(AXLExtensionSchemasContainer.EXTENSION_SCHEMA_DEFINITIONS);
            assertNotNull(linkedObjects);
            assertEquals(propertiesNumber, linkedObjects.size());

            if (linkedObjects.size() != 0) {
                AXLExtensionSchemaDefinition definition = (AXLExtensionSchemaDefinition) linkedObjects.get(0);

                assertFalse(definition.getcontainsUndefinedFields().booleanValue());
                assertTrue(definition.getisNamespaceURIValidURI().booleanValue());
                assertTrue(definition.getisPrefixValidText().booleanValue());
                assertTrue(definition.getisPropertyValidSeq().booleanValue());
                assertTrue(definition.getisSchemaValidText().booleanValue());
                assertTrue(definition.getisValueTypeValidSeq().booleanValue());

                assertEquals("pdfaSchema", definition.getnamespaceURIPrefix());
                assertEquals("pdfaSchema", definition.getprefixPrefix());
                assertEquals("pdfaSchema", definition.getpropertyPrefix());
                assertEquals("pdfaSchema", definition.getschemaPrefix());
                assertNull(definition.getvalueTypePrefix());

                List<? extends org.verapdf.model.baselayer.Object> valueTypes = definition.getLinkedObjects(AXLExtensionSchemaDefinition.EXTENSION_SCHEMA_VALUE_TYPES);
                assertNotNull(valueTypes);
                assertTrue(valueTypes.isEmpty());

                List<? extends org.verapdf.model.baselayer.Object> properties = definition.getLinkedObjects(AXLExtensionSchemaDefinition.EXTENSION_SCHEMA_PROPERTIES);
                assertNotNull(valueTypes);
                assertEquals(1, properties.size());

                if (properties.size() != 0) {
                    AXLExtensionSchemaProperty property = (AXLExtensionSchemaProperty) properties.get(0);
                    assertFalse(property.getcontainsUndefinedFields().booleanValue());
                    assertEquals("external", property.getcategory());
                    assertTrue(property.getisCategoryValidText().booleanValue());
                    assertTrue(property.getisDescriptionValidText().booleanValue());
                    assertTrue(property.getisNameValidText().booleanValue());
                    assertTrue(property.getisValueTypeValidText().booleanValue());
                    assertTrue(property.getisValueTypeDefined().booleanValue());

                    assertEquals("pdfaProperty", property.getcategoryPrefix());
                    assertEquals("pdfaProperty", property.getvalueTypePrefix());
                    assertEquals("pdfaProperty", property.getdescriptionPrefix());
                    assertEquals("pdfaProperty", property.getnamePrefix());
                }
            }
        }
    }
}
