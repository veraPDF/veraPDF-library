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
public class XMPPropertiesNumberTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays
                .asList(new Object[][] {
                        {
                                "/org/verapdf/model/impl/axl/xmp-properties-number-check-1.xml",
                                Integer.valueOf(4), Integer.valueOf(4) },
                        {
                                "/org/verapdf/model/impl/axl/xmp-properties-number-check-2.xml",
                                Integer.valueOf(3), Integer.valueOf(3) },
                        {
                                "/org/verapdf/model/impl/axl/xmp-properties-number-check-3.xml",
                                Integer.valueOf(3), Integer.valueOf(3) },
                        {
                                "/org/verapdf/model/impl/axl/xmp-properties-number-check-4.xml",
                                Integer.valueOf(2), Integer.valueOf(2) } });
    }

    @Parameterized.Parameter
    public String filePath;

    @Parameterized.Parameter(value = 1)
    public Integer metadataPropertiesNumber;

    @Parameterized.Parameter(value = 2)
    public Integer mainMetadataPropertiesNumber;

    @Test
    public void test() throws URISyntaxException, XMPException, IOException {
        try (FileInputStream in = new FileInputStream(
                getSystemIndependentPath(this.filePath))) {
            VeraPDFMeta meta = VeraPDFMeta.parse(in);
            AXLXMPPackage pack = new AXLXMPPackage(meta, true, null,
                    PDFAFlavour.PDFA_1_B);
            int packSize = pack.getLinkedObjects(AXLXMPPackage.PROPERTIES)
                    .size();
            assertEquals(this.metadataPropertiesNumber,
                    Integer.valueOf(packSize));
            AXLMainXMPPackage mainPack = new AXLMainXMPPackage(meta, true,
                    PDFAFlavour.PDFA_1_B);
            int mainPackSize = mainPack.getLinkedObjects(
                    AXLXMPPackage.PROPERTIES).size();
            assertEquals(this.mainMetadataPropertiesNumber,
                    Integer.valueOf(mainPackSize));
        }
    }

    private static String getSystemIndependentPath(String path)
            throws URISyntaxException {
        URL resourceUrl = ClassLoader.class.getResource(path);
        Path resourcePath = Paths.get(resourceUrl.toURI());
        return resourcePath.toString();
    }
}
