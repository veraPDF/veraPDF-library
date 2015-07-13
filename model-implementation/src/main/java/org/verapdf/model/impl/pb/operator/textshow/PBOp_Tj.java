package org.verapdf.model.impl.pb.operator.textshow;

import org.apache.pdfbox.cos.COSBase;

import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_Tj extends PBOpStringTextShow {

	public static final String OP_TJ_TYPE = "Op_Tj";

	public PBOp_Tj(List<COSBase> arguments) {
		super(arguments);
		setType(OP_TJ_TYPE);
	}
}
