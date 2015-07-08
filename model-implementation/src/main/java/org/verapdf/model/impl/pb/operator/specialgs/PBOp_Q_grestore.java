package org.verapdf.model.impl.pb.operator.specialgs;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_Q_grestore;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_Q_grestore extends PBOpSpecialGS implements Op_Q_grestore {

    public static final String OP_Q_GRESTORE_TYPE = "Op_Q_grestore";

    public PBOp_Q_grestore(List<COSBase> arguments) {
        super(arguments);
        setType(OP_Q_GRESTORE_TYPE);
    }

}
