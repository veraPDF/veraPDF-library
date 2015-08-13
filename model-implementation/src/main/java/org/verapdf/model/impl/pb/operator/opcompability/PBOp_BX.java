package org.verapdf.model.impl.pb.operator.opcompability;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_BX;

import java.util.List;

/**
 * Operator which begins a compatibility section.
 *
 * @author Timur Kamalov
 */
public class PBOp_BX extends PBOpCompatibility implements Op_BX {

	/** Type name for {@code PBOp_BX} */
    public static final String OP_BX_TYPE = "Op_BX";

    public PBOp_BX(List<COSBase> arguments) {
        super(arguments, OP_BX_TYPE);
    }

}
