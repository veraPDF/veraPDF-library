package org.verapdf.model.impl.pb.pd;

import org.apache.pdfbox.pdmodel.PDPage;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.BaseTest;
import org.verapdf.model.impl.pb.operator.base.PBOperator;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDContentStreamTest extends BaseTest {

	public static final String FILE_RELATIVE_PATH = "cos/veraPDF test suite 6-1-2-t02-fail-a.pdf";

	@BeforeClass
	public static void setUp() throws URISyntaxException, IOException {
		expectedType = TYPES.contains(PBoxPDContentStream.CONTENT_STREAM_TYPE) ?
																	PBoxPDContentStream.CONTENT_STREAM_TYPE : null;
		expectedID = null;

		setUp(FILE_RELATIVE_PATH);
		PDPage page = document.getPage(0);
		actual = new PBoxPDContentStream(page, page.getResources());
	}

	@Test
	public void testOperatorsLink() {
		List<? extends Object> operators = actual.getLinkedObjects(PBoxPDContentStream.OPERATORS);
		Assert.assertTrue(operators.size() > 0);
		for (Object object : operators) {
			Assert.assertTrue(object instanceof PBOperator);
		}
	}

}
