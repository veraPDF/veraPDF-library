package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.factory.colors.ColorSpaceFactory;
import org.verapdf.model.operator.Op_S_stroke;
import org.verapdf.model.pdlayer.PDColorSpace;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_S_stroke extends PBOpStrokePaint implements Op_S_stroke {

    public static final String OP_S_STROKE_TYPE = "Op_S_stroke";

	public PBOp_S_stroke(List<COSBase> arguments, org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace pbColorSpace) {
        super(arguments, pbColorSpace, null);
        setType(OP_S_STROKE_TYPE);
    }

}
