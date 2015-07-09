package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_s_close_stroke;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_s_close_stroke extends PBOpPathPaint implements Op_s_close_stroke {

    private static final String OP_S_CLOSE_STROKE_TYPE = "Op_s_close_stroke";

    public PBOp_s_close_stroke(List<COSBase> arguments) {
        super(arguments);
        setType(OP_S_CLOSE_STROKE_TYPE);
    }

}
