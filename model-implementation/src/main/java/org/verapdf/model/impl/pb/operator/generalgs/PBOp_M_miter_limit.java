package org.verapdf.model.impl.pb.operator.generalgs;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.operator.Op_M_miter_limit;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_M_miter_limit extends PBOpGeneralGS implements Op_M_miter_limit {

    public static final String OP_M_MITER_LIMIT_TYPE = "Op_M_miter_limit";
	public static final String MITER_LIMIT = "miterLimit";

    public PBOp_M_miter_limit(List<COSBase> arguments) {
        super(arguments);
        setType(OP_M_MITER_LIMIT_TYPE);
    }

	@Override
	public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(String link) {
		List<? extends Object> list;

		switch (link) {
			case MITER_LIMIT:
				list = this.getMiterLimit();
				break;
			default:
				list = super.getLinkedObjects(link);
				break;
		}

		return list;
	}

	private List<CosReal> getMiterLimit() {
		return this.getLastReal(OPERANDS_COUNT);
	}

}
