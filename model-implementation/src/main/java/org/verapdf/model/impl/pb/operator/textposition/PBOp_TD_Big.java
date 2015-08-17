package org.verapdf.model.impl.pb.operator.textposition;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_TD_Big;

import java.util.List;

/**
 * Operator which moves to the start of the next line, offset
 * from the start of the current line by (tx , ty ).
 * As a side effect, this operator sets the leading parameter
 * in the text state
 *
 * @author Evgeniy Muravitskiy
 */
public class PBOp_TD_Big extends PBOp_General_Td implements Op_TD_Big {

	/** Type name for {@code PBOp_TD_Big} */
    public static final String OP_TD_BIG_TYPE = "Op_TD_Big";

    public PBOp_TD_Big(List<COSBase> arguments) {
        super(arguments, OP_TD_BIG_TYPE);
    }
}
