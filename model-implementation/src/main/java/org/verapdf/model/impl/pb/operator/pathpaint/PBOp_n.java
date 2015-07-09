package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_n;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_n extends PBOpPathPaint implements Op_n {

    private static final String OP_N_TYPE = "Op_n";

    public PBOp_n(List<COSBase> arguments) {
        super(arguments, null, null);
        setType(OP_N_TYPE);
    }

}
