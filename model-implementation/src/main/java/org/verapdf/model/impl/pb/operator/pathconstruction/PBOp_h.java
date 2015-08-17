package org.verapdf.model.impl.pb.operator.pathconstruction;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_h;

import java.util.List;

/**
 * Operator which closes the current subpath by appending
 * a straight line segment from the current point to the
 * starting point of the subpath
 *
 * @author Timur Kamalov
 */
public class PBOp_h extends PBOpPathConstruction implements Op_h {

	/** Type name for {@code PBOp_h} */
    public static final String OP_H_TYPE = "Op_h";

    public PBOp_h(List<COSBase> arguments) {
        super(arguments, OP_H_TYPE);
    }

}
