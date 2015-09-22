package org.verapdf.model.impl.pb.operator.textstate;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosName;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.impl.pb.cos.PBCosName;
import org.verapdf.model.operator.Op_Tf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_Tf extends PBOpTextState implements Op_Tf {

	public static final String OP_TF_TYPE = "Op_Tf";

	public static final String SIZE = "size";
	public static final String FONT_NAME = "fontName";

	public PBOp_Tf(List<COSBase> arguments) {
		super(arguments, OP_TF_TYPE);
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case SIZE:
				return this.getSize();
			case FONT_NAME:
				return this.getFontName();
			default:
				return super.getLinkedObjects(link);
		}
	}

	private List<CosReal> getSize() {
		return this.getLastReal();
	}

	private List<CosName> getFontName() {
		if (this.arguments.size() > 1) {
			COSBase base = this.arguments.get(this.arguments.size() - 2);
			if (base instanceof COSName) {
				List<CosName> names = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				names.add(new PBCosName((COSName) base));
				return Collections.unmodifiableList(names);
			}
		}
		return Collections.emptyList();
	}

}
