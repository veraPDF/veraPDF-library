package org.verapdf.model.impl.pb.operator.specialgs;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.OpColor;

import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_CS_Fill extends PBOpSpecialGS implements OpColor {

	public PBOp_CS_Fill(List<COSBase> arguments) {
		super(arguments);
		setType(PBOp_CS_Stroke.OP_COLOR);
	}
}
