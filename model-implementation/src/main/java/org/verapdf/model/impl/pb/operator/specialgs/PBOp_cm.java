package org.verapdf.model.impl.pb.operator.specialgs;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.operator.Op_cm;

import java.util.List;

/**
 * Operator which modifies the current transformation
 * matrix (CTM) by concatenating the specified matrix
 *
 * @author Timur Kamalov
 */
public class PBOp_cm extends PBOpSpecialGS implements Op_cm {

	/** Type name for {@code PBOp_cm} */
    public static final String OP_CM_TYPE = "Op_cm";

	/** Name of link to the concatenate matrix values */
    public static final String MATRIX = "matrix";

    public PBOp_cm(List<COSBase> arguments) {
        super(arguments, OP_CM_TYPE);
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        if (MATRIX.equals(link)) {
            return this.getMatrix();
        }
        return super.getLinkedObjects(link);
    }

    private List<CosReal> getMatrix() {
        return this.getListOfReals();
    }

}
