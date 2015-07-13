package org.verapdf.model.impl.pb.operator.pathconstruction;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.operator.Op_re;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_re extends PBOpPathConstruction implements Op_re {

    public static final String OP_RE_TYPE = "Op_re";

    public static final String RECT_BOX = "rectBox";

    public PBOp_re(List<COSBase> arguments) {
        super(arguments);
        setType(OP_RE_TYPE);
    }

    @Override
    public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(String link) {
        List<? extends org.verapdf.model.baselayer.Object> list;

        switch (link) {
            case RECT_BOX:
                list = this.getRectBox();
                break;
            default: list = super.getLinkedObjects(link);
        }

        return list;
    }

    private List<CosReal> getRectBox() {
        return getListOfReals();
    }

}
