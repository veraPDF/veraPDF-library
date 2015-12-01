package org.verapdf.metadata.fixer.impl.pb.model;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.xmpbox.XMPMetadata;
import org.apache.xmpbox.schema.PDFAIdentificationSchema;
import org.apache.xmpbox.xml.DomXmpParser;
import org.apache.xmpbox.xml.XmpParsingException;
import org.junit.Test;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.MetadataFixerResultImpl;

/**
 * @author Maksim Bezrukov
 */
@SuppressWarnings({ "static-method", "javadoc" })
public class MetadataImplTest {

    @Test
    public void addPDFIdentificationSchemaTest() throws URISyntaxException,
            IOException, XmpParsingException {
        File pdf = new File(getSystemIndependentPath("/test.pdf"));
        try (PDDocument doc = PDDocument.load(pdf, false, true)) {
            PDMetadata meta = doc.getDocumentCatalog().getMetadata();
            DomXmpParser xmpParser = new DomXmpParser();
            try (COSStream cosStream = meta.getStream()) {
                XMPMetadata xmp = xmpParser.parse(cosStream
                        .getUnfilteredStream());
                MetadataImpl impl = new MetadataImpl(xmp, cosStream);
                MetadataFixerResultImpl.Builder builder = new MetadataFixerResultImpl.Builder();
                impl.addPDFIdentificationSchema(builder, PDFAFlavour.PDFA_1_B);

                PDFAIdentificationSchema idCopy = xmp
                        .getPDFIdentificationSchema();

                assertEquals(Integer.valueOf(1), idCopy.getPart());
                assertEquals("B", idCopy.getConformance());
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
