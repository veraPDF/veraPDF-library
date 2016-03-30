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
public class XMPIdentificationTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays
                .asList(new Object[][] {
                        {
                                "/org/verapdf/model/impl/axl/xmp-identification-check-1.xml",
                                Integer.valueOf(1), Long.valueOf(1L), "B",
                                "pdfaid", "pdfaid", "pdfaid", "pdfaid" },
                        {
                                "/org/verapdf/model/impl/axl/xmp-identification-check-2.xml",
                                Integer.valueOf(1), Long.valueOf(2L), "U",
                                "custom", "custom", "custom", "custom" },
                        {
                                "/org/verapdf/model/impl/axl/xmp-identification-check-3.xml",
                                Integer.valueOf(1), null, null, "custom", null,
                                "pdfaid", "pdfaid" },
                        { "/org/verapdf/model/impl/axl/xmp-empty-rdf.xml",
                                Integer.valueOf(0), null, null, null, null,
                                null, null } });
    }

    @Parameterized.Parameter
    public String filePath;

    @Parameterized.Parameter(value = 1)
    public int identificationSchemaNumber;

    @Parameterized.Parameter(value = 2)
    public Long part;

    @Parameterized.Parameter(value = 3)
    public String conformance;

    @Parameterized.Parameter(value = 4)
    public String partPrefix;

    @Parameterized.Parameter(value = 5)
    public String conformancePrefix;

    @Parameterized.Parameter(value = 6)
    public String amdPrefix;

    @Parameterized.Parameter(value = 7)
    public String corrPrefix;

    @Test
    public void test() throws URISyntaxException, XMPException, IOException {
        try (FileInputStream in = new FileInputStream(
                getSystemIndependentPath(this.filePath))) {
            VeraPDFMeta meta = VeraPDFMeta.parse(in);
            AXLMainXMPPackage pack = new AXLMainXMPPackage(meta, true,
                    PDFAFlavour.PDFA_1_B);
            List<? extends org.verapdf.model.baselayer.Object> list = pack
                    .getLinkedObjects(AXLMainXMPPackage.IDENTIFICATION);
            assertEquals(this.identificationSchemaNumber, list.size());
            if (list.size() != 0) {
                AXLPDFAIdentification identification = (AXLPDFAIdentification) list
                        .get(0);
                assertEquals(this.part, identification.getpart());
                assertEquals(this.conformance, identification.getconformance());
                assertEquals(this.partPrefix, identification.getpartPrefix());
                assertEquals(this.conformancePrefix,
                        identification.getconformancePrefix());
                assertEquals(this.amdPrefix, identification.getamdPrefix());
                assertEquals(this.corrPrefix, identification.getcorrPrefix());
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
