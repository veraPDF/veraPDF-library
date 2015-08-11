package org.verapdf.model.impl.pb.operator.pathpaint;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_FStarTest extends PBOpFillPaintTest {

	private static final String CAL_GRAY = "PDCalGray";

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_FStar.OP_FSTAR_TYPE, null);
	}

	@Override
	@Test
	public void testFillColorSpaceLink() {
		testColorSpaceLink(PBOpFillPaint.FILL_CS, CAL_GRAY);
	}

}
