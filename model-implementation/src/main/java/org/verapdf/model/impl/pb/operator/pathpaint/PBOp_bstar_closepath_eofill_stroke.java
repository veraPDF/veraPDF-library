package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import org.verapdf.model.operator.Op_bstar_closepath_eofill_stroke;

import java.util.List;

/**
 * Operator which closes, fills, and then strokes the path.
 * Uses the even-odd rule to determine the region to fill
 *
 * @author Timur Kamalov
 */
public class PBOp_bstar_closepath_eofill_stroke extends PBOpFillAndStroke
        implements Op_bstar_closepath_eofill_stroke {

	/** Type name for {@code PBOp_bstar_closepath_eofill_stroke} */
    public static final String OP_BSTAR_CLOSEPATH_EOFILL_STROKE_TYPE = "Op_bstar_closepath_eofill_stroke";

    public PBOp_bstar_closepath_eofill_stroke(List<COSBase> arguments,
            PDColorSpace strokeColorSpace, PDColorSpace fillColorSpace,
            PDAbstractPattern pattern) {
        super(arguments, pattern, strokeColorSpace, fillColorSpace,
                OP_BSTAR_CLOSEPATH_EOFILL_STROKE_TYPE);
    }
}
