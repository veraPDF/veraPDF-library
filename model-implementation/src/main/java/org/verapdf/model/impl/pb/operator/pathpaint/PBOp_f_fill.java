package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import org.verapdf.model.operator.Op_f_fill;

import java.util.List;

/**
 * Operator which fills the path, using the nonzero winding
 * number rule to determine the region to fill
 *
 * @author Timur Kamalov
 */
public class PBOp_f_fill extends PBOpFillPaint implements Op_f_fill {

	/** Type name for {@code PBOp_f_fill} */
    public static final String OP_F_FILL_TYPE = "Op_f_fill";

    public PBOp_f_fill(List<COSBase> arguments, PDColorSpace pbColorSpace, PDAbstractPattern pattern) {
        super(arguments, pattern, null, pbColorSpace, OP_F_FILL_TYPE);
    }

}
