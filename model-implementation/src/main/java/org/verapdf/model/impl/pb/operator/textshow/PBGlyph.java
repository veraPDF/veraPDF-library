package org.verapdf.model.impl.pb.operator.textshow;

import org.verapdf.model.GenericModelObject;
import org.verapdf.model.operator.Glyph;

/**
 * @author Timur Kamalov
 */
public class PBGlyph extends GenericModelObject implements Glyph {

	private final static String type = "Glyph";

	public PBGlyph() {
	}

	@Override
	public Boolean getisGlyphPresent() {
		return null;
	}

	@Override
	public Boolean getisWidthConsistent() {
		return null;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public String getID() {
		return null;
	}

}
