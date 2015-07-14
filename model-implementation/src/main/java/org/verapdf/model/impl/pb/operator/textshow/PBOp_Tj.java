package org.verapdf.model.impl.pb.operator.textshow;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_Tj;

import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_Tj extends PBOpStringTextShow implements Op_Tj{

	public static final String OP_TJ_TYPE = "Op_Tj";

	public PBOp_Tj(List<COSBase> arguments) {
		super(arguments);
		setType(OP_TJ_TYPE);
	}
}
