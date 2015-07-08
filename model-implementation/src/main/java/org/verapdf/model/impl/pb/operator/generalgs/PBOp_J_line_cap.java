package org.verapdf.model.impl.pb.operator.generalgs;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_J_line_cap;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_J_line_cap extends PBOpGeneralGS implements Op_J_line_cap {

    public static final String OP_J_LINE_CAP_TYPE = "Op_J_line_cap";

    public PBOp_J_line_cap(List<COSBase> arguments) {
        super(arguments);
        setType(OP_J_LINE_CAP_TYPE);
    }

}
