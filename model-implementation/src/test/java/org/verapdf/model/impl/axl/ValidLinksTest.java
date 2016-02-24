package org.verapdf.model.impl.axl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;

/**
 * @author Maksim Bezrukov
 */
@RunWith(Parameterized.class)
public class ValidLinksTest {

    @Parameterized.Parameters
    public static Collection<org.verapdf.model.baselayer.Object> data() {
        return Arrays.asList(new org.verapdf.model.baselayer.Object[]{
                new AXLXMPPackage(null, true, null),
                new AXLMainXMPPackage(null, true),
                new AXLXMPProperty(null, true, false, null, null, null),
                new AXLPDFAIdentification(null),
                new AXLExtensionSchemasContainer(null, null, null),
                new AXLExtensionSchemaDefinition(null, null, null),
                new AXLExtensionSchemaProperty(null, null, null),
                new AXLExtensionSchemaValueType(null, null, null),
                new AXLExtensionSchemaField(null, null, null)
        });
    }

    @Parameterized.Parameter
    public org.verapdf.model.baselayer.Object object;

    @Test
    public void testLinksOfAXLXMPPackage() {
        List<String> links = object.getLinks();
        for (String link : links) {
            assertNotNull(object.getLinkedObjects(link));
        }
    }
}
