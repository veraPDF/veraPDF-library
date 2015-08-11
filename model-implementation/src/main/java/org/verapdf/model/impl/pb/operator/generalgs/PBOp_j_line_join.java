package org.verapdf.model.impl.pb.operator.generalgs;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSInteger;
import org.verapdf.model.coslayer.CosInteger;
import org.verapdf.model.impl.pb.cos.PBCosInteger;
import org.verapdf.model.operator.Op_j_line_join;

import java.util.ArrayList;
import java.util.List;

/**
 * Operator, which set the line join style in the graphics state
 *
 * @author Timur Kamalov
 */
public class PBOp_j_line_join extends PBOpGeneralGS implements Op_j_line_join {

    public static final String LINE_JOIN = "lineJoin";
    public static final String OP_J_LINE_JOIN_TYPE = "Op_j_line_join";

    public PBOp_j_line_join(List<COSBase> arguments) {
        super(arguments, OP_J_LINE_JOIN_TYPE);
    }

    @Override
    public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(
            String link) {
        if (LINE_JOIN.equals(link)) {
            return this.getLineJoin();
        }
        return super.getLinkedObjects(link);
    }

    private List<CosInteger> getLineJoin() {
        List<CosInteger> list = new ArrayList<>(OPERANDS_COUNT);
        if (!this.arguments.isEmpty()) {
			COSBase number = this.arguments.get(arguments.size() - 1);
			if (number instanceof COSInteger) {
				list.add(new PBCosInteger((COSInteger) number));
			}
        }
        return list;
    }
}
