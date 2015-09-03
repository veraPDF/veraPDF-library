package org.verapdf.model.impl.pb.operator.textshow;

import org.junit.Test;
import org.verapdf.model.impl.pb.operator.base.PBOperatorTest;
import org.verapdf.model.impl.pb.pd.colors.PBoxPDDeviceGray;
import org.verapdf.model.impl.pb.pd.font.PBoxPDTrueTypeFont;

/**
 * @author Evgeniy Muravitskiy
 */
public abstract class PBOpTextShowTest extends PBOperatorTest {

	@Test
	public void testFontLink() {
		testObject(PBOpTextShow.FONT, 1, PBoxPDTrueTypeFont.TRUETYPE_FONT_TYPE);
	}

	@Test
	public void testUsedGlyphsLink() {
		testObject(PBOpTextShow.USED_GLYPHS, getUsedGlyphsAmount(), PBGlyph.GLYPH_TYPE);
	}

	@Test
	public void testFillColorSpaceLink() {
		testObject(PBOpTextShow.FILL_COLOR_SPACE,
				((PBOpTextShow) actual).state.getRenderingMode().isFill() ? 1 : 0,
				PBoxPDDeviceGray.DEVICE_GRAY_TYPE);
	}

	@Test
	public void testStrokeColorSpaceLink() {
		testObject(PBOpTextShow.STROKE_COLOR_SPACE,
				((PBOpTextShow) actual).state.getRenderingMode().isStroke() ? 1 : 0,
				PBoxPDDeviceGray.DEVICE_GRAY_TYPE);
	}

	protected abstract int getUsedGlyphsAmount();

}
