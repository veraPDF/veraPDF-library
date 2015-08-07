package org.verapdf.model.impl.pb.operator.pathpaint;

import org.junit.BeforeClass;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_S_strokeTest extends PBOpStrokePaintTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_S_stroke.OP_S_STROKE_TYPE, null);
	}
}
