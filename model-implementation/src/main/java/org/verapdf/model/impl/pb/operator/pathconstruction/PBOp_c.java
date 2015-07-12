package org.verapdf.model.impl.pb.operator.pathconstruction;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.operator.Op_c;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_c extends PBOpPathConstruction implements Op_c {

    public static final String OP_C_TYPE = "Op_c";

    public PBOp_c(List<COSBase> arguments) {
        super(arguments);
        setType(OP_C_TYPE);
    }

    @Override
    public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(String link) {
		List<? extends org.verapdf.model.baselayer.Object> list;

		switch (link) {
			case CONTROL_POINTS:
				list = this.getControlPoints();
				break;
			default:
				list = super.getLinkedObjects(link);
		}

		return list;
	}

	private List<CosReal> getControlPoints() {
		return this.getListOfReals();
	}

}
