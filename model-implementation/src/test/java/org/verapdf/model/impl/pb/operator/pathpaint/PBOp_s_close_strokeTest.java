package org.verapdf.model.impl.pb.operator.pathpaint;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_s_close_strokeTest extends PBOpStrokePaintTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_s_close_stroke.OP_S_CLOSE_STROKE_TYPE, null);
	}

	@Override
	@Test
	public void testStrokeColorSpaceLink() {
		testColorSpaceLink(PBOpStrokePaint.STROKE_CS, PBOp_f_fillTest.DEVICE_GRAY);
	}
}
