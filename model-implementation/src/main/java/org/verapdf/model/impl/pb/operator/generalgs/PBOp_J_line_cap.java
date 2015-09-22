package org.verapdf.model.impl.pb.operator.generalgs;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSInteger;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosInteger;
import org.verapdf.model.impl.pb.cos.PBCosInteger;
import org.verapdf.model.baselayer.*;
import org.verapdf.model.operator.Op_J_line_cap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Operator defining the line cap style in the graphics state
 *
 * @author Timur Kamalov
 */
public class PBOp_J_line_cap extends PBOpGeneralGS
		implements Op_J_line_cap {

	/** Type name for {@code PBOp_J_line_cap} */
    public static final String OP_J_LINE_CAP_TYPE = "Op_J_line_cap";

	/** Name of link to the line cap */
    public static final String LINE_CAP = "lineCap";

    public PBOp_J_line_cap(List<COSBase> arguments) {
        super(arguments, OP_J_LINE_CAP_TYPE);
    }

    @Override
    public List<? extends Object> getLinkedObjects(
            String link) {
        if (LINE_CAP.equals(link)) {
            return this.getLineCap();
        }
        return super.getLinkedObjects(link);
    }

    private List<CosInteger> getLineCap() {
		return this.getLastInteger();
    }

}
