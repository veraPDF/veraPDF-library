package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import org.verapdf.model.operator.Op_F_fill_obsolete;

import java.util.List;

/**
 * Operator equivalent to f
 *
 * @author Timur Kamalov
 */
public class PBOp_F_fill_obsolete extends PBOpFillPaint implements Op_F_fill_obsolete {

	/** Type name for {@code PBOp_F_fill_obsolete} */
    public static final String OP_F_FILL_OBSOLETE_TYPE = "Op_F_fill_obsolete";

    public PBOp_F_fill_obsolete(List<COSBase> arguments, PDColorSpace colorSpace, PDAbstractPattern pattern) {
        super(arguments, pattern, null, colorSpace, OP_F_FILL_OBSOLETE_TYPE);
    }

}
