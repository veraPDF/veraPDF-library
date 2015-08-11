package org.verapdf.model.impl.pb.operator.pathpaint;

import org.junit.BeforeClass;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_BStar_eofill_strokeTest extends PBOpStrokeAndFillPaintTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_BStar_eofill_stroke.OP_BSTAR_EOFILL_STROKE_TYPE, null);
	}
}
