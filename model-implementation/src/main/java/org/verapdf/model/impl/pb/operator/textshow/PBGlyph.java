package org.verapdf.model.impl.pb.operator.textshow;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.verapdf.model.GenericModelObject;
import org.verapdf.model.operator.Glyph;
import org.verapdf.model.tools.IDGenerator;

/**
 * @author Timur Kamalov
 */
public class PBGlyph extends GenericModelObject implements Glyph {

	public final static String GLYPH_TYPE = "Glyph";

	private final String id;

	private Boolean glyphPresent;
	private Boolean widthsConsistent;

	public PBGlyph(Boolean glyphPresent, Boolean widthsConsistent, PDFont font, int glyphCode) {
		this(glyphPresent, widthsConsistent, font, glyphCode, GLYPH_TYPE);
	}

	public PBGlyph(Boolean glyphPresent, Boolean widthsConsistent, PDFont font, int glyphCode, String type) {
		super(type);
		this.glyphPresent = glyphPresent;
		this.widthsConsistent = widthsConsistent;

		this.id = IDGenerator.generateID(font.getCOSObject().hashCode(), font.getName(), glyphCode);
	}

	// TODO : implement me
	public String getname() {
		return null;
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
		return id;
	}

}