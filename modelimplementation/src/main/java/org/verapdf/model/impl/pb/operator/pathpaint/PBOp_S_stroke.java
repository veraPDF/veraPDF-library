package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_S_stroke;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_S_stroke extends PBOpPathPaint implements Op_S_stroke {

    public static final String OP_S_STROKE_TYPE = "Op_S_stroke";

    public PBOp_S_stroke(List<COSBase> arguments) {
        super(arguments);
        setType(OP_S_STROKE_TYPE);
    }

}
