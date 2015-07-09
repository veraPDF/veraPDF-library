package org.verapdf.model.impl.pb.operator.pathconstruction;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSFloat;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.impl.pb.cos.PBCosReal;
import org.verapdf.model.operator.Op_y;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_y extends PBOpPathConstruction implements Op_y {

    public static final String OP_Y_TYPE = "Op_y";

    public static final String CONTROL_POINTS = "controlPoints";

    public PBOp_y(List<COSBase> arguments) {
        super(arguments);
        setType(OP_Y_TYPE);
    }

    @Override
    public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(String link) {
        List<? extends org.verapdf.model.baselayer.Object> list;

        switch (link) {
            case CONTROL_POINTS:
                list = this.getControlPoints();
                break;
            default: list = super.getLinkedObjects(link);
        }

        return list;
    }

    private List<CosReal> getControlPoints() {
        List<CosReal> list = new ArrayList<>();
        if (!this.arguments.isEmpty() && this.arguments.get(0) instanceof COSArray) {
            for (COSBase arg : (COSArray) this.arguments.get(0)) {
                if (arg instanceof COSFloat) {
                    list.add(new PBCosReal((COSFloat) arg));
                }

            }
        }
        return list;
    }

}
