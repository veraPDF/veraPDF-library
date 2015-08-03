package org.verapdf.model.impl.pb.operator.pathconstruction;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.operator.Op_l;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_l extends PBOpPathConstruction implements Op_l {

    public static final String OP_L_TYPE = "Op_l";

    public static final String POINT = "point";

    public PBOp_l(List<COSBase> arguments) {
        super(arguments, OP_L_TYPE);
    }

    @Override
    public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(String link) {
        if (POINT.equals(link)) {
            return this.getPoint();
        }
        return super.getLinkedObjects(link);
    }

    private List<CosReal> getPoint() {
        return this.getListOfReals();
    }

}
