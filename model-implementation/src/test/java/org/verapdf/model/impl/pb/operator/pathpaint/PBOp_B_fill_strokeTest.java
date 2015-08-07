package org.verapdf.model.impl.pb.operator.pathpaint;

import org.junit.BeforeClass;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_B_fill_strokeTest extends PBOpStrokeAndFillPaintTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_B_fill_stroke.OP_B_FILL_STROKE_TYPE, null);
	}
}
