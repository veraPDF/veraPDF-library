package org.verapdf.model.impl.pb.operator.generalgs;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_gs;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_gs extends PBOpGeneralGS implements Op_gs {

    public static final String OP_GS_TYPE = "OP_gs";

    public PBOp_gs(List<COSBase> arguments) {
        super(arguments);
        setType(OP_GS_TYPE );
    }

}
