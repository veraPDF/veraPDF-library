package org.verapdf.model.impl.pb.operator.pathconstruction;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_v;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_v extends PBOpPathConstruction implements Op_v {

    public static final String OP_V_TYPE = "Op_v";

    public PBOp_v(List<COSBase> arguments) {
        super(arguments);
        setType(OP_V_TYPE);
    }

}
