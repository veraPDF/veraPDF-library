package org.verapdf.model.impl.pb.operator.textstate;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.operator.Op_Tc;

import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_Tc extends PBOpTextState implements Op_Tc {

	public static final String OP_TC_TYPE = "Op_Tc";

	public static final String CHAR_SPACING = "charSpace";

	public PBOp_Tc(List<COSBase> arguments) {
		super(arguments, OP_TC_TYPE);
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		if (CHAR_SPACING.equals(link)) {
			return this.getCharSpacing();
		}
		return super.getLinkedObjects(link);
	}

	private List<CosReal> getCharSpacing() {
		return this.getLastReal();
	}
}
