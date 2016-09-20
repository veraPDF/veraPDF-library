package org.verapdf.model.impl.axl;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.impl.VeraPDFMeta;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

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
                                "/org/verapdf/model/impl/axl/xmp-extension-schema-container-type-check-1.xml",
                                Boolean.TRUE, "pdfaExtension", Integer.valueOf(1)},
                        {
                                "/org/verapdf/model/impl/axl/xmp-extension-schema-container-type-check-2.xml",
                                Boolean.FALSE, "ext" ,  Integer.valueOf(1)},
                        {
                                "/org/verapdf/model/impl/axl/xmp-extension-schema-container-type-check-3.xml",
                                Boolean.FALSE, "pdfaExtension" ,  Integer.valueOf(1)},
                        {
                                "/org/verapdf/model/impl/axl/xmp-extension-schema-container-type-check-4.xml",
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
        try (FileInputStream in = new FileInputStream(
                getSystemIndependentPath(this.filePath))) {
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

    private static String getSystemIndependentPath(String path)
            throws URISyntaxException {
        URL resourceUrl = ClassLoader.class.getResource(path);
        Path resourcePath = Paths.get(resourceUrl.toURI());
        return resourcePath.toString();
    }
}
