package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import org.verapdf.model.operator.Op_S_stroke;

import java.util.List;

/**
 * Operator which strokes the path
 *
 * @author Timur Kamalov
 */
public class PBOp_S_stroke extends PBOpStrokePaint implements Op_S_stroke {

    public static final String OP_S_STROKE_TYPE = "Op_S_stroke";

    public PBOp_S_stroke(List<COSBase> arguments,
            org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace pbColorSpace,
            PDAbstractPattern pattern) {
        super(arguments, pattern, pbColorSpace, null, OP_S_STROKE_TYPE);
    }

}
