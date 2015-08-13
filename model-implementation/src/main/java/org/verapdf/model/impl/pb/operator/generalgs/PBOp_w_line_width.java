package org.verapdf.model.impl.pb.operator.generalgs;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.operator.Op_w_line_width;

import java.util.List;

/**
 * Operator defining the line width in the graphics state
 *
 * @author Timur Kamalov
 */
public class PBOp_w_line_width extends PBOpGeneralGS
		implements Op_w_line_width {

	/** Type name for {@code PBOp_w_line_width} */
	public static final String OP_W_LINE_WIDTH_TYPE = "Op_w_line_width";

	/** Name of link to the width for */
    public static final String LINE_WIDTH = "lineWidth";

    public PBOp_w_line_width(List<COSBase> arguments) {
        super(arguments, OP_W_LINE_WIDTH_TYPE);
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        if (LINE_WIDTH.equals(link)) {
            return this.getLineWidth();
        }
        return super.getLinkedObjects(link);
    }

    private List<CosReal> getLineWidth() {
        return this.getLastReal();
    }

}
