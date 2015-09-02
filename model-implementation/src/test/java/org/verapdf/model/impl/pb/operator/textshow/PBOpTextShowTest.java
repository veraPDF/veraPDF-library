package org.verapdf.model.impl.pb.operator.textshow;

import org.junit.Ignore;
import org.junit.Test;
import org.verapdf.model.impl.pb.operator.base.PBOperatorTest;
import org.verapdf.model.impl.pb.pd.colors.PBoxPDDeviceGray;

/**
 * @author Evgeniy Muravitskiy
 */
public abstract class PBOpTextShowTest extends PBOperatorTest {

	@Ignore
	@Test
	public void testFontLink() {
		// TODO : implement me
	}

	@Ignore
	@Test
	public void testUsedGlyphsLink() {
		// TODO : implement me
	}

	@Test
	public void testFillColorSpaceLink() {
		testColorSpace(PBOpTextShow.FILL_COLOR_SPACE);
	}

	@Test
	public void testStrokeColorSpaceLink() {
		testColorSpace(PBOpTextShow.STROKE_COLOR_SPACE);
	}

	private void testColorSpace(String fillColorSpace) {
		getObject(fillColorSpace, 1, PBoxPDDeviceGray.DEVICE_GRAY_TYPE);
	}

}
