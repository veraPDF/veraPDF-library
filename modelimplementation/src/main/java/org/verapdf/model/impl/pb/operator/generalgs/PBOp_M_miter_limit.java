package org.verapdf.model.impl.pb.operator.generalgs;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_M_miter_limit;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_M_miter_limit extends PBOpGeneralGS implements Op_M_miter_limit {

    public static final String OP_M_MITER_LIMIT_TYPE = "Op_M_miter_limit";

    public PBOp_M_miter_limit(List<COSBase> arguments) {
        super(arguments);
        setType(OP_M_MITER_LIMIT_TYPE);
    }

}
