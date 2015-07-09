package org.verapdf.model.impl.pb.operator.generalgs;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_i;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_i extends PBOpGeneralGS implements Op_i {

    public static final String OP_I_TYPE = "Op_i";

    public PBOp_i(List<COSBase> arguments) {
        super(arguments);
        setType(OP_I_TYPE);
    }

}
