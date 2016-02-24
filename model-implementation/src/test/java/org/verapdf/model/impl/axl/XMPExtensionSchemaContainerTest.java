package org.verapdf.model.impl.axl;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.impl.VeraPDFMeta;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * @author Maksim Bezrukov
 */
@RunWith(Parameterized.class)
public class XMPExtensionSchemaContainerTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"/model/impl/axl/xmp-extension-schema-container-type-check-1.xml", true, "pdfaExtension"},
                {"/model/impl/axl/xmp-extension-schema-container-type-check-2.xml", false, "ext"},
                {"/model/impl/axl/xmp-extension-schema-container-type-check-3.xml", false, "pdfaExtension"},
                {"/model/impl/axl/xmp-extension-schema-container-type-check-4.xml", false, "smth"}
        });
    }

    @Parameterized.Parameter
    public String filePath;

    @Parameterized.Parameter(value = 1)
    public Boolean isValidValueType;

    @Parameterized.Parameter(value = 2)
    public String prefix;

    @Test
    public void test() throws URISyntaxException, FileNotFoundException, XMPException {
        FileInputStream in = new FileInputStream(getSystemIndependentPath(filePath));
        VeraPDFMeta meta = VeraPDFMeta.parse(in);
        AXLXMPPackage pack = new AXLXMPPackage(meta, true, null);
        AXLExtensionSchemasContainer container = (AXLExtensionSchemasContainer) pack.getLinkedObjects(AXLXMPPackage.EXTENSION_SCHEMAS_CONTAINERS).get(0);
        assertEquals(isValidValueType, container.getisValidBag());
        assertEquals(prefix, container.getprefix());
    }

    private static String getSystemIndependentPath(String path)
            throws URISyntaxException {
        URL resourceUrl = ClassLoader.class.getResource(path);
        Path resourcePath = Paths.get(resourceUrl.toURI());
        return resourcePath.toString();
    }
}
