package org.verapdf.model.impl.pb.pd.font;

import org.verapdf.model.pdlayer.PDTrueTypeFont;

/**
 * @author Timur Kamalov
 */
public class PBoxPDTrueTypeFont extends PBoxPDSimpleFont implements PDTrueTypeFont {

	public static final String TRUETYPE_FONT_TYPE = "PDTrueTypeFont";

	public PBoxPDTrueTypeFont(org.apache.pdfbox.pdmodel.font.PDTrueTypeFont font) {
		super(font, TRUETYPE_FONT_TYPE);
	}

	// TODO : implement me
	@Override
	public Boolean getdifferencesAreUnicodeCompliant() {
		return Boolean.FALSE;
	}

	@Override
	public Boolean getisStandard() {
		return Boolean.FALSE;
	}

}
