package org.verapdf.model.impl.pb.pd.colors;

import org.junit.AfterClass;
import org.junit.Assert;
import org.verapdf.model.impl.BaseTest;
import org.verapdf.model.pdlayer.PDColorSpace;

import java.io.IOException;

/**
 * @author Evgeniy Muravitskiy
 */
public abstract class PBoxPDColorSpaceTest extends BaseTest {

	protected static final String FILE_RELATIVE_PATH = "pd/ColorSpaces.pdf";

	protected void testNumberOfComponentsMethod(long value) {
		Assert.assertEquals(Long.valueOf(value), ((PDColorSpace) actual).getnrComponents());
	}

	@AfterClass
	public static void tearDown() throws IOException {
		expectedType = null;
		expectedID = null;
		actual = null;

		document.close();
	}
}
