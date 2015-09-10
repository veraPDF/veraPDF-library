package org.verapdf.model.impl.pb.operator.textstate;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.operator.Op_Tl;

import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_Tl extends PBOpTextState implements Op_Tl {

	public static final String OP_TL_TYPE = "Op_Tl";

	public static final String LEADING = "leading";

	public PBOp_Tl(List<COSBase> arguments) {
		super(arguments, OP_TL_TYPE);
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		if (LEADING.equals(link)) {
			return this.getLeading();
		}
		return super.getLinkedObjects(link);
	}

	private List<CosReal> getLeading() {
		return this.getLastReal();
	}

}
