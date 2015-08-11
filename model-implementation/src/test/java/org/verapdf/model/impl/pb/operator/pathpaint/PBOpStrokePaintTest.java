package org.verapdf.model.impl.pb.operator.pathpaint;

import org.junit.Test;

/**
 * @author Evgeniy Muravitskiy
 */
public abstract class PBOpStrokePaintTest extends PBOpPathPaintTest {

	@Test
	public void testStrokeColorSpaceLink() {
		testColorSpaceLink(PBOpStrokePaint.STROKE_CS, DEVICE_RGB);
	}
}
