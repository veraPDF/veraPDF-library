package org.verapdf.model.impl.pb.operator.textstate;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSInteger;
import org.verapdf.model.operator.Op_Tr;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_Tr extends PBOpTextState implements Op_Tr {

    public static final String OP_TR_TYPE = "Op_Tr";

    public PBOp_Tr(List<COSBase> arguments) {
        super(arguments, OP_TR_TYPE);
    }

    @Override
    public Long getrenderingMode() {
        if (!arguments.isEmpty()) {
            COSBase renderingMode = arguments.get(0);
            if (renderingMode instanceof COSInteger) {
                return Long.valueOf(((COSInteger) renderingMode).longValue());
            }
        }
        return null;
    }
}
