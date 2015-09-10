package org.verapdf.model.impl.pb.operator.textstate;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.factory.font.FontFactory;
import org.verapdf.model.pdlayer.PDFont;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_Tf extends PBOpTextState implements Op_Tf {

	public static final String OP_TF_TYPE = "Op_Tf";

	public static final String SIZE = "size";
	public static final String FONT = "font";

	private final org.apache.pdfbox.pdmodel.font.PDFont font;

	public PBOp_Tf(List<COSBase> arguments, org.apache.pdfbox.pdmodel.font.PDFont font) {
		super(arguments, OP_TF_TYPE);
		this.font = font;
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case SIZE:
				return this.getSize();
			case FONT:
				return this.getFont();
			default:
				return super.getLinkedObjects(link);
		}
	}

	private List<CosReal> getSize() {
		return this.getLastReal();
	}

	private List<PDFont> getFont() {
		PDFont pdFont = FontFactory.parseFont(this.font);
		if (pdFont != null) {
			List<PDFont> fonts = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			fonts.add(pdFont);
			return fonts;
		}

		return Collections.emptyList();
	}

	private COSName getFontName() {
		if (this.arguments.size() > 1) {
			COSBase base = this.arguments.get(this.arguments.size() - 2);
			return base instanceof COSName ? (COSName) base : null;
		}
		return null;
	}
}
