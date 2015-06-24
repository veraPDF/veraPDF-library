package org.verapdf.model.impl.pb.operator.pathconstruction;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_re;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_re extends PBOpPathConstruction implements Op_re {

    public static final String OP_RE_TYPE = "Op_re";

    public PBOp_re(List<COSBase> arguments) {
        super(arguments);
        setType(OP_RE_TYPE);
    }

}
