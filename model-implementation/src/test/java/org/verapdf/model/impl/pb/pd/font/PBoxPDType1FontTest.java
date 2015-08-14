package org.verapdf.model.impl.pb.pd.font;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Timur Kamalov
 */
public class PBoxPDType1FontTest extends PBoxPDSimpleFontTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		expectedType = TYPES.contains(PBoxPDSimpleFont.SIMPLE_FONT_TYPE) ? PBoxPDSimpleFont.SIMPLE_FONT_TYPE : null;
		System.out.println();
	}

	@Override
	@Test
	public void testSubtypeMethod() {
		System.out.println();
	}
}
