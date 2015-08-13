package org.verapdf.model.impl.pb.operator.opcompability;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_Undefined;

import java.util.List;

/**
 * Any operator which not defined by PDF Reference
 *
 * @author Timur Kamalov
 */
public class PBOp_Undefined extends PBOpCompatibility implements Op_Undefined {

	/** Type name for {@code PBOp_Undefined} */
    public static final String OP_UNDEFINED_TYPE = "Op_Undefined";

    public PBOp_Undefined(List<COSBase> arguments) {
        super(arguments, OP_UNDEFINED_TYPE);
    }

}
