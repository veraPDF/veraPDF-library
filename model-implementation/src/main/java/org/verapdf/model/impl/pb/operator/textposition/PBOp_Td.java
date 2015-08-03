package org.verapdf.model.impl.pb.operator.textposition;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_Td;

import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_Td extends PBOp_General_Td implements Op_Td {

    public static final String OP_TD_TYPE = "Op_Td";

    public PBOp_Td(List<COSBase> arguments) {
        super(arguments, OP_TD_TYPE);
    }
}
