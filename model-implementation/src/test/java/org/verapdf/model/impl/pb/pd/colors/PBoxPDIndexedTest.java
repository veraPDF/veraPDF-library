package org.verapdf.model.impl.pb.pd.colors;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.color.PDIndexed;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDIndexedTest extends PBoxPDColorSpaceTest {

	private static final String COLOR_SPACE_NAME = "IndexedCS";

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		expectedType = TYPES.contains(PBoxPDIndexed.INDEXED_TYPE) ? PBoxPDIndexed.INDEXED_TYPE : null;
		expectedID = null;

		setUp(FILE_RELATIVE_PATH);
		PDColorSpace indexed = document.getPage(0).getResources().getColorSpace(COSName.getPDFName(COLOR_SPACE_NAME));
		actual = new PBoxPDIndexed((PDIndexed) indexed);
	}

	@Test
	public void testNumberOfComponentsMethod() {
		super.testNumberOfComponentsMethod(1);
	}

	@Test
	public void testBaseLink() {
		List<? extends Object> base = actual.getLinkedObjects(PBoxPDIndexed.BASE);
		Assert.assertEquals(1, base.size());
		for (Object object : base) {
			Assert.assertTrue(object instanceof org.verapdf.model.pdlayer.PDColorSpace);
		}
	}
}
