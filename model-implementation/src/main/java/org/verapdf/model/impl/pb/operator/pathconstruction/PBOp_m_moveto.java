package org.verapdf.model.impl.pb.operator.pathconstruction;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.operator.Op_m_moveto;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_m_moveto extends PBOpPathConstruction implements Op_m_moveto {

    public static final String OP_M_MOVETO_TYPE = "Op_m_moveto";

    public static final String POINT = "point";

    public PBOp_m_moveto(List<COSBase> arguments) {
        super(arguments, OP_M_MOVETO_TYPE);
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
