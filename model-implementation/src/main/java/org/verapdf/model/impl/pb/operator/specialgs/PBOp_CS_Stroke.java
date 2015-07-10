package org.verapdf.model.impl.pb.operator.specialgs;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.OpColor;

import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_CS_Stroke extends PBOpSpecialGS implements OpColor {

	public static final String OP_COLOR = "OpColor";

	public PBOp_CS_Stroke(List<COSBase> arguments) {
		super(arguments);
		setType(OP_COLOR);
	}
}
