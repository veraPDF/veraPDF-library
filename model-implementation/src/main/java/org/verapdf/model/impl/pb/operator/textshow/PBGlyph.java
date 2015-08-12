package org.verapdf.model.impl.pb.operator.textshow;

import org.verapdf.model.GenericModelObject;
import org.verapdf.model.operator.Glyph;

/**
 * @author Timur Kamalov
 */
public class PBGlyph extends GenericModelObject implements Glyph {

	private final static String type = "Glyph";

	private Boolean glyphPresent;
	private Boolean widthsConsistent;

	//used for id
	private String fontName;
	private int glyphCode;

	public PBGlyph(Boolean glyphPresent, Boolean widthsConsistent, String fontName, int glyphCode) {
		this.glyphPresent = glyphPresent;
		this.widthsConsistent = widthsConsistent;

		this.fontName = fontName;
		this.glyphCode = glyphCode;
	}

	@Override
	public Boolean getisGlyphPresent() {
		return glyphPresent;
	}

	@Override
	public Boolean getisWidthConsistent() {
		return widthsConsistent;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public String getID() {
		return fontName + " " + glyphCode;
	}

}
