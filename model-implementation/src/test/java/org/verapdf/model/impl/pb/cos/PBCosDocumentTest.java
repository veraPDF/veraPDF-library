package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosDocument;
import org.verapdf.model.coslayer.CosTrailer;
import org.verapdf.model.coslayer.CosXRef;
import org.verapdf.model.impl.BaseTest;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBCosDocumentTest extends BaseTest {

    public static final String FILE_RELATIVE_PATH = "/model/impl/pb/cos/veraPDF test suite 6-1-2-t02-fail-a.pdf";

    private static final Long expectedNumberOfIndirects = Long.valueOf(17);
    private static final Long expectedSizeOfDocument = Long.valueOf(9437);
	private static final Double expectedDocumentVersion = Double.valueOf(1.4);
    private static final String[] expectedIDS = new String[]{"D6CF927DCF82444068EB69A5914F8070",
            "A2A7539F7C71DEBB6A4A6B418235962D"};


    @BeforeClass
    public static void setUp() throws URISyntaxException, IOException {
        expectedType = TYPES.contains(PBCosDocument.COS_DOCUMENT_TYPE) ? PBCosDocument.COS_DOCUMENT_TYPE : null;
        expectedID = null;

        String fileAbsolutePath = getSystemIndependentPath(FILE_RELATIVE_PATH);
        final File file = new File(fileAbsolutePath);
        try (PDDocument document = PDDocument.load(file, Boolean.FALSE, Boolean.TRUE)) {
            actual = new PBCosDocument(document, file.length());
        }
    }

    @Test
    public void testNumberOfIndirectsMethod() {
        Assert.assertEquals(expectedNumberOfIndirects, ((CosDocument) actual).getnrIndirects());
    }

	@Test
	public void testVersionMethod() {
		Assert.assertEquals(expectedDocumentVersion, ((CosDocument) actual).getversion(), 0.01);
	}

    @Test
    public void testSizeMethod() {
        Assert.assertEquals(expectedSizeOfDocument, ((CosDocument) actual).getsize());
    }

    @Test
    public void testBinaryHeaderMethod() {
        Assert.assertFalse(((CosDocument) actual).getbinaryHeaderComplyPDFA());
    }

    @Test
    public void testPDFHeaderMethod() {
        Assert.assertTrue(((CosDocument) actual).getpdfHeaderCompliesPDFA());
    }

    @Test
    public void testOptionalContentMethod() {
        Assert.assertFalse(((CosDocument) actual).getisOptionalContentPresent());
    }

    @Test
    public void testEOFMethod() {
        Assert.assertTrue(((CosDocument) actual).geteofCompliesPDFA());
    }

    @Test
    public void testFirstPageTrailer() {
        String[] actualIDs = ((CosDocument) actual).getfirstPageID().split(" ");
        Assert.assertEquals(expectedIDS.length, actualIDs.length);
        for (int index = 0; index < actualIDs.length; index++) {
            Assert.assertEquals(getExpectedID(index), actualIDs[index]);
        }
    }

    @Test
    public void testLastTrailer() {
        String[] actualIDs = ((CosDocument) actual).getlastID().split(" ");
        Assert.assertEquals(expectedIDS.length, actualIDs.length);
        for (int index = 0; index < actualIDs.length; index++) {
            Assert.assertEquals(getExpectedID(index), actualIDs[index]);
        }
    }

    // problems with code symbols
    private String getExpectedID(int index) {
        StringBuilder builder = new StringBuilder(16);
        for (int i = 0; i < expectedIDS[index].length(); i += 2) {
            builder.append((char) Integer.parseInt(expectedIDS[index].substring(i, i + 2), 16));
        }
        return builder.toString();
    }

    @Test
    public void testIsLinearized() {
        Assert.assertFalse(((CosDocument) actual).getisLinearized());
    }

    @Test
    public void testInfoMatchXMP() {
        Assert.assertTrue(((CosDocument) actual).getdoesInfoMatchXMP());
    }

    @Test
    public void testTrailerLink() {
        List<? extends Object> trailer = actual.getLinkedObjects(PBCosDocument.TRAILER);
        Assert.assertEquals(1, trailer.size());
        Assert.assertTrue(trailer.get(0) != null && trailer.get(0) instanceof CosTrailer);
    }

    @Test
    public void testIndirectObjectsLink() {
        List<? extends Object> indirects = actual.getLinkedObjects(PBCosDocument.INDIRECT_OBJECTS);
        Assert.assertEquals(expectedNumberOfIndirects.intValue(), indirects.size());
        for (Object indirect : indirects) {
            Assert.assertTrue(indirect != null);
            Assert.assertTrue(indirect instanceof PBCosIndirect);
        }
    }

    @Test
    public void testDocumentLink() {
        List<? extends Object> document = actual.getLinkedObjects(PBCosDocument.DOCUMENT);
        Assert.assertEquals(1, document.size());
        Assert.assertTrue(document.get(0) != null);
        Assert.assertTrue(document.get(0) instanceof org.verapdf.model.pdlayer.PDDocument);
    }

    @Test
    public void testXREFLink() {
        List<? extends Object> xref = actual.getLinkedObjects(PBCosDocument.XREF);
        Assert.assertEquals(1, xref.size());
        Assert.assertTrue(xref.get(0) != null);
        Assert.assertTrue(xref.get(0) instanceof CosXRef);
    }

    @Test
    public void testEmbeddedFilesLink() {
        List<? extends Object> embeddedFiles = actual.getLinkedObjects(PBCosDocument.EMBEDDED_FILES);
        Assert.assertEquals(0, embeddedFiles.size());
    }

    @AfterClass
    public static void tearDown() {
        expectedType = null;
        expectedID = null;
        actual = null;
    }
}
