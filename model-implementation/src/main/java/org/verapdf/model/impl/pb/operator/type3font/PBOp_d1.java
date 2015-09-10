package org.verapdf.model.impl.pb.operator.type3font;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.operator.Op_d1;

import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_d1 extends PBOpType3Font implements Op_d1 {

	public static final String OP_D1_TYPE = "Op_d1";

	public static final String CONTROL_POINTS = "controlPoints";

	public PBOp_d1(List<COSBase> arguments) {
		super(arguments, OP_D1_TYPE);
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		if (CONTROL_POINTS.equals(link)) {
			return this.getControlPoints();
		}
		return super.getLinkedObjects(link);
	}

	private List<CosReal> getControlPoints() {
		return this.getListOfReals();
	}
}
