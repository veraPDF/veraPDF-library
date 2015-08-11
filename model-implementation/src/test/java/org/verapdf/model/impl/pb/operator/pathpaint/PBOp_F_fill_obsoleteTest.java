package org.verapdf.model.impl.pb.operator.pathpaint;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_F_fill_obsoleteTest extends PBOpFillPaintTest {

	private static final String TILLING_PATTERN = "PDTilingPattern";

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_F_fill_obsolete.OP_F_FILL_OBSOLETE_TYPE, null);
	}

	@Override
	@Test
	public void testFillColorSpaceLink() {
		testColorSpaceLink(PBOpFillPaint.FILL_CS, TILLING_PATTERN);
	}
}
