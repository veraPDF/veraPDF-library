package org.verapdf.model.impl.pb.operator.textshow;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.factory.operator.GraphicState;
import org.verapdf.model.operator.Op_Quote;
import org.verapdf.model.tools.resources.PDInheritableResources;

import java.util.List;

/**
 * Operator which moves to the next line and shows a text string
 *
 * @author Evgeniy Muravitskiy
 */
public class PBOp_Quote extends PBOpStringTextShow implements Op_Quote {

	/** Type name for {@code PBOp_Quote} */
    public static final String OP_QUOTE_TYPE = "Op_Quote";

    public PBOp_Quote(List<COSBase> arguments, GraphicState state, PDInheritableResources resources) {
        super(arguments, state, resources, OP_QUOTE_TYPE);
    }
}
