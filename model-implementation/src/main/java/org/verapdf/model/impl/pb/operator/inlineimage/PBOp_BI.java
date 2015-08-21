package org.verapdf.model.impl.pb.operator.inlineimage;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_BI;

import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_BI extends PBOpInlineImage implements Op_BI {

	public static final String OP_BI_TYPE = "Op_BI";

	public PBOp_BI(List<COSBase> arguments) {
		super(arguments, OP_BI_TYPE);
	}
}
