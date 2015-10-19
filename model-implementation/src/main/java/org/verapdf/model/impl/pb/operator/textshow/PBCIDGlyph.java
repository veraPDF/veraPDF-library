package org.verapdf.model.impl.pb.operator.textshow;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.verapdf.model.operator.CIDGlyph;

/**
 * @author Timur Kamalov
 */
public class PBCIDGlyph extends PBGlyph implements CIDGlyph {

	public final static String CID_GLYPH_TYPE = "CIDGlyph";

	private int CID;

	public PBCIDGlyph(Boolean glyphPresent, Boolean widthsConsistent, PDFont font, int glyphCode, int CID) {
		super(glyphPresent, widthsConsistent, font, glyphCode, CID_GLYPH_TYPE);
		this.CID = CID;
	}

	@Override
	public Long getCID() {
		return Long.valueOf(CID);
	}
}
