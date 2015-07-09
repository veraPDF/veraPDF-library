package org.verapdf.model.impl.pb.operator.pathconstruction;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_h;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_h extends PBOpPathConstruction implements Op_h {

    public static final String OP_H_TYPE = "Op_h";

    public PBOp_h(List<COSBase> arguments) {
        super(arguments);
        setType(OP_H_TYPE);
    }

}
