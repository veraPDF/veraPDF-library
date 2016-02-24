package org.verapdf.metadata.fixer.impl.pb.model;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.impl.VeraPDFMeta;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.MetadataFixerResultImpl;

import java.io.File;
import java.io.IOException;
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
@SuppressWarnings({"static-method", "javadoc"})
@RunWith(Parameterized.class)
public class MetadataImplTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"/test1.pdf", 1, "B"}, {"/test2.pdf", 1, "B"}
        });
    }

    @Parameterized.Parameter
    public String filePath;

    @Parameterized.Parameter(value = 1)
    public Integer filePart;

    @Parameterized.Parameter(value = 2)
    public String fileConformance;

    @Test
    public void addPDFIdentificationSchemaTest() throws URISyntaxException,
            IOException, XMPException {
        File pdf = new File(getSystemIndependentPath(filePath));
        try (PDDocument doc = PDDocument.load(pdf, false, true)) {
            PDMetadata meta = doc.getDocumentCatalog().getMetadata();
            try (COSStream cosStream = meta.getStream()) {
                VeraPDFMeta xmp = VeraPDFMeta.parse(cosStream
                        .getUnfilteredStream());
                MetadataImpl impl = new MetadataImpl(xmp, cosStream);
                MetadataFixerResultImpl.Builder builder = new MetadataFixerResultImpl.Builder();
                impl.addPDFIdentificationSchema(builder, PDFAFlavour.PDFA_1_B);

                assertEquals(filePart, xmp.getIdentificationPart());
                assertEquals(fileConformance, xmp.getIdentificationConformance());
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
