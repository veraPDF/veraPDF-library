package org.verapdf.model.impl.pb.operator.textshow;

import org.apache.pdfbox.cos.COSBase;

import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_Quote extends PBOpStringTextShow {

	public static final String OP_QUOTE_TYPE = "Op_Quote";

	public PBOp_Quote(List<COSBase> arguments) {
		super(arguments);
		setType(OP_QUOTE_TYPE);
	}
}
