package org.verapdf.model.impl.pb.operator.specialgs;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_cm;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_cm extends PBOpSpecialGS implements Op_cm {

    public static final String OP_CM_TYPE = "Op_cm";

    public PBOp_cm(List<COSBase> arguments) {
        super(arguments);
        setType(OP_CM_TYPE);
    }

}
