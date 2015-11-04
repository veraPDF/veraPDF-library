package org.verapdf.model.impl.pb.operator.textshow;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.factory.operator.GraphicState;
import org.verapdf.model.operator.Op_Tj;
import org.verapdf.model.tools.resources.PDInheritableResources;

import java.util.List;

/**
 * Operator which shows a text string
 *
 * @author Evgeniy Muravitskiy
 */
public class PBOp_Tj extends PBOpStringTextShow implements Op_Tj {

	/** Type name for {@code PBOp_Tj} */
    public static final String OP_TJ_TYPE = "Op_Tj";

    public PBOp_Tj(List<COSBase> arguments,
				   GraphicState state,
				   PDInheritableResources resources) {
        super(arguments, state, resources, OP_TJ_TYPE);
    }
}
