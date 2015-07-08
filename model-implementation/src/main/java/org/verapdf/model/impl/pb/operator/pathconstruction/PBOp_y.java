package org.verapdf.model.impl.pb.operator.pathconstruction;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_y;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_y extends PBOpPathConstruction implements Op_y {

    public static final String OP_Y_TYPE = "Op_y";

    public PBOp_y(List<COSBase> arguments) {
        super(arguments);
        setType(OP_Y_TYPE);
    }

}
