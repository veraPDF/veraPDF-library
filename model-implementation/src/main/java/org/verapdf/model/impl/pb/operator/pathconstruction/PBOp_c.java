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
        super(arguments, OP_C_TYPE);
    }

    @Override
    public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(
            String link) {
        if (CONTROL_POINTS.equals(link)) {
            return this.getControlPoints();
        }
        return super.getLinkedObjects(link);
    }

    private List<CosReal> getControlPoints() {
        return this.getListOfReals();
    }

}
