package org.verapdf.model.impl.pb.operator.pathconstruction;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSFloat;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.impl.pb.cos.PBCosReal;
import org.verapdf.model.operator.Op_m_moveto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_m_moveto extends PBOpPathConstruction implements Op_m_moveto {

    public static final String OP_M_MOVETO_TYPE = "Op_m_moveto";

    public static final String POINT = "point";

    public PBOp_m_moveto(List<COSBase> arguments) {
        super(arguments);
        setType(OP_M_MOVETO_TYPE);
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
