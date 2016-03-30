package org.verapdf.model.impl.axl;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

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
public class XMPExtensionSchemaContainerTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays
                .asList(new Object[][] {
                        {
                                "/org/verapdf/model/impl/axl/xmp-extension-schema-container-type-check-1.xml",
                                Boolean.TRUE, "pdfaExtension" },
                        {
                                "/org/verapdf/model/impl/axl/xmp-extension-schema-container-type-check-2.xml",
                                Boolean.FALSE, "ext" },
                        {
                                "/org/verapdf/model/impl/axl/xmp-extension-schema-container-type-check-3.xml",
                                Boolean.FALSE, "pdfaExtension" },
                        {
                                "/org/verapdf/model/impl/axl/xmp-extension-schema-container-type-check-4.xml",
                                Boolean.FALSE, "smth" } });
    }

    @Parameterized.Parameter
    public String filePath;

    @Parameterized.Parameter(value = 1)
    public Boolean isValidValueType;

    @Parameterized.Parameter(value = 2)
    public String prefix;

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
        }
    }

    private static String getSystemIndependentPath(String path)
            throws URISyntaxException {
        URL resourceUrl = ClassLoader.class.getResource(path);
        Path resourcePath = Paths.get(resourceUrl.toURI());
        return resourcePath.toString();
    }
}
