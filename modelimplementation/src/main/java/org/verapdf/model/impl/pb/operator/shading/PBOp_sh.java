package org.verapdf.model.impl.pb.operator.shading;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_sh;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_sh extends PBOpShading implements Op_sh {

    public static final String OP_SH_TYPE = "Op_sh";

    public PBOp_sh(List<COSBase> arguments) {
        super(arguments);
        setType(OP_SH_TYPE);
    }

}
