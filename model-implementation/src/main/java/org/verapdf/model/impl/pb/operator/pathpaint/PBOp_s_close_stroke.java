package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import org.verapdf.model.operator.Op_s_close_stroke;

import java.util.List;

/**
 * Operator, which close and stroke the path
 *
 * @author Timur Kamalov
 */
public class PBOp_s_close_stroke extends PBOpStrokePaint implements
        Op_s_close_stroke {

	/** Type name for {@code PBOp_s_close_stroke} */
    public static final String OP_S_CLOSE_STROKE_TYPE = "Op_s_close_stroke";

    public PBOp_s_close_stroke(List<COSBase> arguments,
            org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace pbColorSpace,
            PDAbstractPattern pattern) {
        super(arguments, pattern, pbColorSpace, null, OP_S_CLOSE_STROKE_TYPE);
    }

}
