package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.factory.operator.GraphicState;
import org.verapdf.model.operator.Op_B_fill_stroke;
import org.verapdf.model.tools.resources.PDInheritableResources;

import java.util.List;

/**
 * Operator which fills and then strokes the path. Uses the
 * nonzero winding number rule to determine the region to fill
 *
 * @author Timur Kamalov
 */
public class PBOp_B_fill_stroke extends PBOpFillAndStroke implements
		Op_B_fill_stroke {

	/** Type name for {@code PBOp_B_fill_stroke} */
	public static final String OP_B_FILL_STROKE_TYPE = "Op_B_fill_stroke";

	/**
	 * Default constructor
	 *
	 * @param arguments arguments for current operator, must be empty.
	 * @param state graphic state for current operator
	 * @param resources resources for tilling pattern if it`s used
	 */
	public PBOp_B_fill_stroke(List<COSBase> arguments,
							  final GraphicState state,
							  final PDInheritableResources resources) {
		super(arguments, state, resources, OP_B_FILL_STROKE_TYPE);
	}

}
