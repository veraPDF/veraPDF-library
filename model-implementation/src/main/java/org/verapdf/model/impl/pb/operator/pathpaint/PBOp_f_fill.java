package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_f_fill;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_f_fill extends PBOpPathPaint implements Op_f_fill {

    private static final String OP_F_FILL_TYPE = "Op_f_fill";

    public PBOp_f_fill(List<COSBase> arguments) {
        super(arguments);
        setType(OP_F_FILL_TYPE);
    }

}
