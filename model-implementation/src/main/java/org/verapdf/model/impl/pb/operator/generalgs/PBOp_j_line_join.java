package org.verapdf.model.impl.pb.operator.generalgs;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSInteger;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosInteger;
import org.verapdf.model.impl.pb.cos.PBCosInteger;
import org.verapdf.model.operator.Op_j_line_join;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Operator defining the line join style in the graphics state
 *
 * @author Timur Kamalov
 */
public class PBOp_j_line_join extends PBOpGeneralGS implements Op_j_line_join {

	/** Type name for {@code PBOp_j_line_join} */
	public static final String OP_J_LINE_JOIN_TYPE = "Op_j_line_join";

	/** Name of link to the line join for */
    public static final String LINE_JOIN = "lineJoin";

    public PBOp_j_line_join(List<COSBase> arguments) {
        super(arguments, OP_J_LINE_JOIN_TYPE);
    }

    @Override
    public List<? extends Object> getLinkedObjects(
            String link) {
        if (LINE_JOIN.equals(link)) {
            return this.getLineJoin();
        }
        return super.getLinkedObjects(link);
    }

	private List<CosInteger> getLineJoin() {
		return this.getLastInteger();
	}
}
