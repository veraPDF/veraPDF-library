package org.verapdf.model.impl.pb.operator.textshow;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.verapdf.model.operator.Op_Tj;

import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_Tj extends PBOpStringTextShow implements Op_Tj{

	public static final String OP_TJ_TYPE = "Op_Tj";

	public PBOp_Tj(List<COSBase> arguments, PDFont font) {
		super(arguments, font);
		setType(OP_TJ_TYPE);
	}
}
