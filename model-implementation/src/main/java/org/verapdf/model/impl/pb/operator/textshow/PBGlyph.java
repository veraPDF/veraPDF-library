package org.verapdf.model.impl.pb.operator.textshow;

import org.verapdf.model.GenericModelObject;
import org.verapdf.model.operator.Glyph;
import org.verapdf.model.tools.IDGenerator;

/**
 * @author Timur Kamalov
 */
public class PBGlyph extends GenericModelObject implements Glyph {

	public final static String GLYPH_TYPE = "Glyph";

	private Boolean glyphPresent;
	private Boolean widthsConsistent;

	//used for id
	private String fontName;
	private int glyphCode;

	public PBGlyph(Boolean glyphPresent, Boolean widthsConsistent, String fontName, int glyphCode) {
		super(GLYPH_TYPE);
		this.glyphPresent = glyphPresent;
		this.widthsConsistent = widthsConsistent;

		this.fontName = fontName;
		this.glyphCode = glyphCode;
	}

	public PBGlyph(Boolean glyphPresent, Boolean widthsConsistent, String fontName, int glyphCode, String type) {
		super(type);
		this.glyphPresent = glyphPresent;
		this.widthsConsistent = widthsConsistent;

		this.fontName = fontName;
		this.glyphCode = glyphCode;
	}

	@Override
	public Boolean getisGlyphPresent() {
		return this.glyphPresent;
	}

	@Override
	public Boolean getisWidthConsistent() {
		return this.widthsConsistent;
	}

	@Override
	public String getID() {
		return IDGenerator.generateID(this.fontName, this.glyphCode);
	}

}
