package org.verapdf.model.impl.pb.cos;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.*;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosDocument;
import org.verapdf.model.coslayer.CosTrailer;
import org.verapdf.model.coslayer.CosXRef;
import org.verapdf.model.impl.BaseTest;

import static org.verapdf.model.impl.pb.cos.PBCosDocument.*;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBCosDocumentTest extends BaseTest {

    public static final String FILE_RELATIVE_PATH = "/model/impl/pb/cos/veraPDF test suite 6-1-2-t02-fail-a.pdf";

    private static final Long expectedNumberOfIndirects = Long.valueOf(17);
    private static final Long expectedSizeOfDocument = Long.valueOf(9437);
	private static final double expectedDocumentVersion = 1.4;
    private static final String[] expectedIDS = new String[]{"D6CF927DCF82444068EB69A5914F8070",
            "A2A7539F7C71DEBB6A4A6B418235962D"};


    @BeforeClass
    public static void setUp() throws URISyntaxException, IOException {
        expectedType = TYPES.contains(COS_DOCUMENT_TYPE) ? COS_DOCUMENT_TYPE : null;
        expectedID = null;

        String fileAbsolutePath = getSystemIndependentPath(FILE_RELATIVE_PATH);
        final File file = new File(fileAbsolutePath);
        try (PDDocument doc = PDDocument.load(file, false, true)) {
            actual = new PBCosDocument(doc, file.length());
        }
    }

    @Test
    public void testNumberOfIndirectsMethod() {
        Assert.assertEquals(expectedNumberOfIndirects, ((CosDocument) actual).getnrIndirects());
    }

	@Test
	public void testVersionMethod() {
		double actualVersion = ((CosDocument) actual).getversion().doubleValue();
		Assert.assertEquals(expectedDocumentVersion, actualVersion, 0.01);
	}

    @Test
    public void testSizeMethod() {
		Long actual = ((CosDocument) BaseTest.actual).getsize();
		Assert.assertEquals(expectedSizeOfDocument, actual);
    }

	@Test
	public void testHeaderOffset() {
		Long actualOffset = ((CosDocument) BaseTest.actual).getheaderOffset();
		Assert.assertEquals(Long.valueOf(0), actualOffset);
	}

	@Test
	public void testHeader() {
		String actualHeader = ((CosDocument) actual).getheader();
		Assert.assertEquals("%PDF-1.4", actualHeader);
	}

	@Test
	public void testHeaderByte1() {
		Long actualHeaderByte = ((CosDocument) BaseTest.actual).getheaderByte1();
		Assert.assertEquals(Long.valueOf(-1), actualHeaderByte);
	}

	@Test
	public void testHeaderByte2() {
		Long actualHeaderByte = ((CosDocument) BaseTest.actual).getheaderByte2();
		Assert.assertEquals(Long.valueOf(-1), actualHeaderByte);
	}

	@Test
	public void testHeaderByte3() {
		Long actualHeaderByte = ((CosDocument) BaseTest.actual).getheaderByte3();
		Assert.assertEquals(Long.valueOf(-1), actualHeaderByte);
	}

	@Test
	public void testHeaderByte4() {
		Long actualHeaderByte = ((CosDocument) BaseTest.actual).getheaderByte4();
		Assert.assertEquals(Long.valueOf(-1), actualHeaderByte);
	}

    @Test
    public void testOptionalContentMethod() {
        Assert.assertFalse(((CosDocument) actual).getisOptionalContentPresent().booleanValue());
    }

    @Test
    public void testPostEOFDate() {
		Assert.assertEquals(Long.valueOf(0), ((CosDocument) actual).getpostEOFDataSize());
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
    private static String getExpectedID(int index) {
        StringBuilder builder = new StringBuilder(16);
        for (int i = 0; i < expectedIDS[index].length(); i += 2) {
            builder.append((char) Integer.parseInt(expectedIDS[index].substring(i, i + 2), 16));
        }
        return builder.toString();
    }

    @Test
    public void testIsLinearized() {
        Assert.assertFalse(((CosDocument) actual).getisLinearized().booleanValue());
    }

    @Test
    public void testInfoMatchXMP() {
        Assert.assertTrue(((CosDocument) actual).getdoesInfoMatchXMP().booleanValue());
    }

    @Test
    public void testTrailerLink() {
        List<? extends Object> trailer = actual.getLinkedObjects(PBCosDocument.TRAILER);
        Assert.assertEquals(1, trailer.size());
        Assert.assertTrue(trailer.get(0) instanceof CosTrailer);
    }

    @Test
    public void testIndirectObjectsLink() {
        List<? extends Object> indirects = actual.getLinkedObjects(PBCosDocument.INDIRECT_OBJECTS);
        Assert.assertEquals(expectedNumberOfIndirects.intValue(), indirects.size());
        for (Object indirect : indirects) {
            Assert.assertTrue(indirect instanceof PBCosIndirect);
        }
    }

    @Test
    public void testDocumentLink() {
        List<? extends Object> doc = actual.getLinkedObjects(PBCosDocument.DOCUMENT);
        Assert.assertEquals(1, doc.size());
        Assert.assertTrue(doc.get(0) instanceof org.verapdf.model.pdlayer.PDDocument);
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

}
