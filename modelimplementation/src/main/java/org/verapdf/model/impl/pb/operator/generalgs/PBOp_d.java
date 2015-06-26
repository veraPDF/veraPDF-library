package org.verapdf.model.impl.pb.operator.generalgs;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_d;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_d extends PBOpGeneralGS implements Op_d {

    public static final String OP_D_TYPE = "Op_d";

    public PBOp_d(List<COSBase> arguments) {
        super(arguments);
        setType(OP_D_TYPE);
    }

}
