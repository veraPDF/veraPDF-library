package org.verapdf.model.impl.pb.operator.textstate;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.operator.Op_Ts;

import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_Ts extends PBOpTextState implements Op_Ts {

	public static final String OP_TS_TYPE = "Op_Ts";

	public static final String RISE = "rise";

	public PBOp_Ts(List<COSBase> arguments) {
		super(arguments, OP_TS_TYPE);
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		if (RISE.equals(link)) {
			return this.getRise();
		}
		return super.getLinkedObjects(link);
	}

	private List<CosReal> getRise() {
		return this.getLastReal();
	}
}
