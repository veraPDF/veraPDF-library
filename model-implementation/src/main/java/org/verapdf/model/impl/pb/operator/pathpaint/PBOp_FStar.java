package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import org.verapdf.model.operator.Op_FStar;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_FStar extends PBOpFillPaint implements Op_FStar {

    public static final String OP_FSTAR_TYPE = "Op_FStar";

    public PBOp_FStar(List<COSBase> arguments, PDColorSpace colorSpace, PDAbstractPattern pattern) {
        super(arguments, pattern, null, colorSpace, OP_FSTAR_TYPE);
    }

}
