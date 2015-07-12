package org.verapdf.model.impl.pb.operator.generalgs;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.coslayer.CosArray;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.impl.pb.cos.PBCosArray;
import org.verapdf.model.operator.Op_d;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_d extends PBOpGeneralGS implements Op_d {

    public static final String OP_D_TYPE = "Op_d";

    public static final String DASH_ARRAY = "dashArray";
    public static final String DASH_PHASE = "dashPhase";

    public PBOp_d(List<COSBase> arguments) {
        super(arguments);
        setType(OP_D_TYPE);
    }

    @Override
    public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(String link) {
        List<? extends org.verapdf.model.baselayer.Object> list;

        switch (link) {
            case DASH_ARRAY:
                list = this.getDashArray();
                break;
            case DASH_PHASE:
                list = getDashPhase();
                break;
            default: list = super.getLinkedObjects(link);
        }

        return list;
    }

	private List<CosArray> getDashArray() {
        List<CosArray> list = new ArrayList<>(OPERANDS_COUNT);
        if (!arguments.isEmpty() && arguments.get(0) instanceof COSArray) {
            list.add(new PBCosArray((COSArray) arguments.get(0)));
        }
        return list;
    }

	private List<CosReal> getDashPhase() {
		return this.getLastReal(OPERANDS_COUNT);
	}

}
