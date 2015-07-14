package org.verapdf.model.impl.pb.operator.textposition;

import org.apache.pdfbox.cos.COSBase;

import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_TD_Big extends PBOp_General_Td {

	public static final String OP_TD_BIG_TYPE = "Op_Td_Big";

	public PBOp_TD_Big(List<COSBase> arguments) {
		super(arguments);
		setType(OP_TD_BIG_TYPE);
	}
}
