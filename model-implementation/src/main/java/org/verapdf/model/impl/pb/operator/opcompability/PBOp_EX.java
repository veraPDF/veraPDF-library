package org.verapdf.model.impl.pb.operator.opcompability;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_EX;

import java.util.List;

/**
 * Operator, which end a compatibility section
 *
 * @author Timur Kamalov
 */
public class PBOp_EX extends PBOpCompatibility implements Op_EX {

    public static final String OP_EX_TYPE = "Op_EX";

    public PBOp_EX(List<COSBase> arguments) {
        super(arguments, OP_EX_TYPE);
    }

}