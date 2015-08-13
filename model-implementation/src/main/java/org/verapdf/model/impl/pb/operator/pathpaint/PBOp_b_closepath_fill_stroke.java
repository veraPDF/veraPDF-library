package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import org.verapdf.model.operator.Op_b_closepath_fill_stroke;

import java.util.List;

/**
 * Operator which closes, fills and then strokes the pat. Uses the
 * nonzero winding number rule to determine the region to fill
 *
 * @author Timur Kamalov
 */
public class PBOp_b_closepath_fill_stroke extends PBOpFillAndStroke implements
        Op_b_closepath_fill_stroke {

	/** Type name for {@code PBOp_b_closepath_fill_stroke} */
    public static final String OP_B_CLOSEPATH_FILL_STROKE_TYPE = "Op_b_closepath_fill_stroke";

    public PBOp_b_closepath_fill_stroke(List<COSBase> arguments,
            PDColorSpace strokeColorSpace, PDColorSpace fillColorSpace,
            PDAbstractPattern pattern) {
        super(arguments, pattern, strokeColorSpace, fillColorSpace,
                OP_B_CLOSEPATH_FILL_STROKE_TYPE);
    }

}
