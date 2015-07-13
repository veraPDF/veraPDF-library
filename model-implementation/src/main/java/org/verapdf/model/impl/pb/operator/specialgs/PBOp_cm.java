package org.verapdf.model.impl.pb.operator.specialgs;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.impl.pb.cos.PBCosReal;
import org.verapdf.model.operator.Op_cm;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_cm extends PBOpSpecialGS implements Op_cm {

    public static final String OP_CM_TYPE = "Op_cm";

    public static final String MATRIX = "matrix";

    public PBOp_cm(List<COSBase> arguments) {
        super(arguments);
        setType(OP_CM_TYPE);
    }

    @Override
    public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(String link) {
        List<? extends org.verapdf.model.baselayer.Object> list;

        switch (link) {
            case MATRIX:
                list = this.getMatrix();
                break;
            default: list = super.getLinkedObjects(link);
        }

        return list;
    }

    private List<CosReal> getMatrix() {
        List<CosReal> list = new ArrayList<>();
        if (!this.arguments.isEmpty() && this.arguments.get(0) instanceof COSArray) {
            for (COSBase arg : (COSArray) this.arguments.get(0)) {
                if (arg instanceof COSNumber) {
                    list.add(new PBCosReal((COSNumber) arg));
                }

            }
        }
        return list;
    }


}
