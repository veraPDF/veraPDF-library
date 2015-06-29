package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_b_closepath_fill_stroke;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_b_closepath_fill_stroke extends PBOpPathPaint implements Op_b_closepath_fill_stroke {

    private static final String OP_B_CLOSEPATH_FILL_STROKE_TYPE = "Op_b_closepath_fill_stroke";

    public PBOp_b_closepath_fill_stroke(List<COSBase> arguments) {
        super(arguments);
        setType(OP_B_CLOSEPATH_FILL_STROKE_TYPE);
    }

}
