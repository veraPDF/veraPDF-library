package org.verapdf.model.impl.pb.operator.textstate;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.operator.Op_Tz;

import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_Tz extends PBOpTextState implements Op_Tz{

	public static final String OP_TZ_TYPE = "Op_Tz";

	public static final String SCALE = "scale";

	public PBOp_Tz(List<COSBase> arguments) {
		super(arguments);
		setType(OP_TZ_TYPE);
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		List<? extends Object> list;

		switch (link) {
			case SCALE:
				list = this.getScale();
				break;
			default:
				list = super.getLinkedObjects(link);
				break;
		}

		return list;
	}

	private List<CosReal> getScale() {
		return this.getLastReal();
	}
}
