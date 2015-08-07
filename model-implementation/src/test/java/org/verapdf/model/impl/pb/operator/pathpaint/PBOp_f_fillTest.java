package org.verapdf.model.impl.pb.operator.pathpaint;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_f_fillTest extends PBOpFillPaintTest {

	public static final String DEVICE_GRAY = "PDDeviceGray";

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_f_fill.OP_F_FILL_TYPE, null);
	}

	@Test
	public void testFillColorSpaceLink() {
		testColorSpaceLink(PBOpFillPaint.FILL_CS, DEVICE_GRAY);
	}
}
