package org.verapdf.model.impl.pb.operator.generalgs;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_ri;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_ri extends PBOpGeneralGS implements Op_ri {

    public static final String OP_RI_TYPE = "Op_ri";

    public PBOp_ri(List<COSBase> arguments) {
        super(arguments);
        setType(OP_RI_TYPE);
    }

}
