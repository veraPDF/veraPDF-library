package org.verapdf.model.impl.pb.external;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.external.ICCOutputProfile;
import org.verapdf.model.impl.pb.cos.PBCosDocumentTest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxICCOutputProfileTest extends PBoxICCProfileTest {

	private static final String expectedS = "GTS_PDFA1";

	private static PDDocument doc;

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		expectedType = TYPES.contains(PBoxICCOutputProfile.ICC_OUTPUT_PROFILE_TYPE) ?
																PBoxICCOutputProfile.ICC_OUTPUT_PROFILE_TYPE : null;
		expectedID = null;

		setUpActualObject();
	}

	private static void setUpActualObject() throws URISyntaxException, IOException {
		String fileAbsolutePath = getSystemIndependentPath(PBCosDocumentTest.FILE_RELATIVE_PATH);
		File file = new File(fileAbsolutePath);

		doc = PDDocument.load(file, false, true);
		PDOutputIntent outputIntent = doc.getDocumentCatalog().getOutputIntents().get(0);
		InputStream unfilteredStream = outputIntent.getDestOutputIntent().getUnfilteredStream();
		Long N = Long.valueOf(outputIntent.getDestOutputIntent().getLong(COSName.N));
		actual = new PBoxICCOutputProfile(unfilteredStream, COSName.GTS_PDFA1.getName(), N);
	}

	@Test
	public void testSMethod() {
		Assert.assertEquals(expectedS, ((ICCOutputProfile) actual).getS());
	}

	@AfterClass
	public static void tearDown() throws IOException {
		expectedType = null;
		expectedID = null;
		actual = null;

		doc.close();
	}
}
