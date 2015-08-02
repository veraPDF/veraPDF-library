package org.verapdf.model.impl.pb.operator.pathpaint;

import java.util.List;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import org.verapdf.model.operator.Op_s_close_stroke;

/**
 * @author Timur Kamalov
 */
public class PBOp_s_close_stroke extends PBOpStrokePaint implements Op_s_close_stroke {

    public static final String OP_S_CLOSE_STROKE_TYPE = "Op_s_close_stroke";

	public PBOp_s_close_stroke(List<COSBase> arguments, org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace pbColorSpace,
							   PDAbstractPattern pattern) {
        super(arguments, pattern, pbColorSpace, null);
        setType(OP_S_CLOSE_STROKE_TYPE);
    }

}
