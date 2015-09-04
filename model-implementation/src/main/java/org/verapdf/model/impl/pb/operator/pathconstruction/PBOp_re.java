package org.verapdf.model.impl.pb.operator.pathconstruction;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.operator.Op_re;

import java.util.List;

/**
 * Operator which appends a rectangle to the current path
 * as a complete sub path, with lower-left corner (x, y)
 * and dimensions width and height in user space
 *
 * @author Timur Kamalov
 */
public class PBOp_re extends PBOpPathConstruction implements Op_re {

	/** Type name for {@code PBOp_re} */
    public static final String OP_RE_TYPE = "Op_re";

	/** Name of link to the rectangle box */
    public static final String RECT_BOX = "rectBox";

    public PBOp_re(List<COSBase> arguments) {
        super(arguments, OP_RE_TYPE);
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
       if(RECT_BOX.equals(link)) {
           return this.getRectBox();
       }
       return super.getLinkedObjects(link);
    }

    private List<CosReal> getRectBox() {
        return this.getListOfReals();
    }

}
