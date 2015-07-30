package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import org.verapdf.model.operator.Op_BStar_eofill_stroke;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_BStar_eofill_stroke extends PBOpFillAndStroke implements Op_BStar_eofill_stroke {

    private static final String OP_BSTAR_EOFILL_STROKE_TYPE = "Op_BStar_eofill_stroke";

	public PBOp_BStar_eofill_stroke(List<COSBase> arguments, PDColorSpace strokeColorSpace,
									PDColorSpace fillColorSpace, PDAbstractPattern pattern) {
		super(arguments, pattern, strokeColorSpace, fillColorSpace);
		setType(OP_BSTAR_EOFILL_STROKE_TYPE);
	}

}
