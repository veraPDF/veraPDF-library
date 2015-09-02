package org.verapdf.model.impl.pb.operator.textstate;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSInteger;
import org.verapdf.model.operator.Op_Tr;

import java.util.List;

/**
 * Operator defining the text rendering mode
 *
 * @author Timur Kamalov
 */
public class PBOp_Tr extends PBOpTextState implements Op_Tr {

	/** Type name for {@code PBOp_Tr} */
    public static final String OP_TR_TYPE = "Op_Tr";

    public PBOp_Tr(List<COSBase> arguments) {
        super(arguments, OP_TR_TYPE);
    }

	/**
	 * @return rendering mode
	 */
    @Override
    public Long getrenderingMode() {
        if (!this.arguments.isEmpty()) {
            COSBase renderingMode = this.arguments.get(0);
            if (renderingMode instanceof COSInteger) {
                return Long.valueOf(((COSInteger) renderingMode).longValue());
            }
        }
        return null;
    }
}
