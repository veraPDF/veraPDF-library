package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_bstar_closepath_eofill_stroke;
import org.verapdf.model.pdlayer.PDColorSpace;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_bstar_closepath_eofill_stroke extends PBOpFillAndStroke implements Op_bstar_closepath_eofill_stroke {

    public static final String OP_BSTAR_CLOSEPATH_EOFILL_STROKE_TYPE = "Op_bstar_closepath_eofill_stroke";

	public PBOp_bstar_closepath_eofill_stroke(List<COSBase> arguments,
											  org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace strokeColorSpace,
											  org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace fillColorSpace) {
		super(arguments, strokeColorSpace, fillColorSpace);
		setType(OP_BSTAR_CLOSEPATH_EOFILL_STROKE_TYPE);
	}

}
