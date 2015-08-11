package org.verapdf.model.impl.pb.operator.pathconstruction;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.operator.Op_y;

import java.util.List;

/**
 * Operator, which append a cubic Bézier curve to the current path
 *
 * @author Timur Kamalov
 */
public class PBOp_y extends PBOpPathConstruction implements Op_y {

    public static final String OP_Y_TYPE = "Op_y";

    public PBOp_y(List<COSBase> arguments) {
        super(arguments, OP_Y_TYPE);
    }

    @Override
    public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(String link) {
        if (CONTROL_POINTS.equals(link)) {
            return this.getControlPoints();
        }
        return super.getLinkedObjects(link);
    }

    private List<CosReal> getControlPoints() {
        return this.getListOfReals();
    }

}
