package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_BStar_eofill_stroke;
import org.verapdf.model.pdlayer.PDColorSpace;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_BStar_eofill_stroke extends PBOpPathPaint implements Op_BStar_eofill_stroke {

    private static final String OP_BSTAR_EOFILL_STROKE_TYPE = "Op_BStar_eofill_stroke";

    public static final String FILL_CS = "fillCS";
    public static final String STROKE_CS = "strokeCS";

	public PBOp_BStar_eofill_stroke(List<COSBase> arguments,
									org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace strokeColorSpace,
									org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace fillColorSpace) {
		super(arguments, strokeColorSpace, fillColorSpace);
		setType(OP_BSTAR_EOFILL_STROKE_TYPE);
	}

}
