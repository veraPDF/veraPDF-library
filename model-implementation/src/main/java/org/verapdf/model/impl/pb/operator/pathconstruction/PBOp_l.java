package org.verapdf.model.impl.pb.operator.pathconstruction;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.operator.Op_l;

import java.util.List;

/**
 * Operator which appends a straight line segment
 * from the current point to the to the point (x, y)
 *
 * @author Timur Kamalov
 */
public class PBOp_l extends PBOpPathConstruction implements Op_l {

	/** Type name for {@code PBOp_l} */
    public static final String OP_L_TYPE = "Op_l";

	/** Name of link to the point */
    public static final String POINT = "point";

    public PBOp_l(List<COSBase> arguments) {
        super(arguments, OP_L_TYPE);
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        if (POINT.equals(link)) {
            return this.getPoint();
        }
        return super.getLinkedObjects(link);
    }

    private List<CosReal> getPoint() {
        return this.getListOfReals();
    }

}
