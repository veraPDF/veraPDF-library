package org.verapdf.model.impl.pb.operator.generalgs;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSInteger;
import org.verapdf.model.coslayer.CosInteger;
import org.verapdf.model.impl.pb.cos.PBCosInteger;
import org.verapdf.model.operator.Op_j_line_join;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_j_line_join extends PBOpGeneralGS implements Op_j_line_join {

	public static final String LINE_JOIN = "lineJoin";
    public static final String OP_J_LINE_JOIN_TYPE = "Op_j_line_join";

    public PBOp_j_line_join(List<COSBase> arguments) {
        super(arguments);
        setType(OP_J_LINE_JOIN_TYPE);
    }

	@Override
	public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(String link) {
		List<? extends org.verapdf.model.baselayer.Object> list;

		switch (link) {
			case LINE_JOIN:
				list = this.getLineJoin();
				break;
			default:
				list = super.getLinkedObjects(link);
				break;
		}

		return list;
	}

	private List<CosInteger> getLineJoin() {
		List<CosInteger> list = new ArrayList<>(OPERANDS_COUNT);
		if (!this.arguments.isEmpty() && this.arguments.get(arguments.size() - 1) instanceof COSInteger) {
			list.add(new PBCosInteger((COSInteger) this.arguments.get(arguments.size() - 1)));
		}
		return list;
	}
}
