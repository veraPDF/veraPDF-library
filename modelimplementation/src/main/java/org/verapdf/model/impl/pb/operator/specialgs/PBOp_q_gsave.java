package org.verapdf.model.impl.pb.operator.specialgs;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.impl.pb.operator.generalgs.PBOpGeneralGS;
import org.verapdf.model.operator.Op_q_gsave;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_q_gsave extends PBOpGeneralGS implements Op_q_gsave {

    public static final String OP_Q_GSAVE_TYPE = "Op_q_gsave";

    public PBOp_q_gsave(List<COSBase> arguments) {
        super(arguments);
        setType(OP_Q_GSAVE_TYPE);
    }

    @Override
    public Long getnestingLevel() {
        return null;
    }

}
