package org.verapdf.model.impl.pb.operator.markedcontent;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_EMC;

import java.util.List;

/**
 * Operator which ends a marked-content sequence
 *
 * @author Timur Kamalov
 */
public class PBOp_EMC extends PBOpMarkedContent implements Op_EMC {

	/** Type name for {@code PBOp_EMC} */
    public static final String OP_EMC_TYPE = "Op_EMC";

    public PBOp_EMC(List<COSBase> arguments) {
        super(arguments, OP_EMC_TYPE);
    }

}
