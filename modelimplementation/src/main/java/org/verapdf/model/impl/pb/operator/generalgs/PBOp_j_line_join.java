package org.verapdf.model.impl.pb.operator.generalgs;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_j_line_join;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_j_line_join extends PBOpGeneralGS implements Op_j_line_join {

    public static final String OP_J_LINE_JOIN_TYPE = "Op_j_line_join";

    public PBOp_j_line_join(List<COSBase> arguments) {
        super(arguments);
        setType(OP_J_LINE_JOIN_TYPE);
    }

}
