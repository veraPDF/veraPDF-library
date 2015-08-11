package org.verapdf.model.impl.pb.operator.pathpaint;

import org.junit.Test;

/**
 * @author Evgeniy Muravitskiy
 */
public abstract class PBOpStrokeAndFillPaintTest extends PBOpPathPaintTest {

	@Test
	public void testStrokeColorSpaceLink() {
		testColorSpaceLink(PBOpStrokePaint.STROKE_CS, DEVICE_RGB);
	}

	@Test
	public void testFillColorSpaceLink() {
		testColorSpaceLink(PBOpFillPaint.FILL_CS, DEVICE_RGB);
	}
}
