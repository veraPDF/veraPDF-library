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
        super(arguments);
        setType(OP_L_TYPE);
    }

    @Override
    public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(String link) {
        List<? extends org.verapdf.model.baselayer.Object> list;

        switch (link) {
            case POINT:
                list = this.getPoint();
                break;
            default: list = super.getLinkedObjects(link);
        }

        return list;
    }

    private List<CosReal> getPoint() {
        return this.getListOfReals();
    }

}
