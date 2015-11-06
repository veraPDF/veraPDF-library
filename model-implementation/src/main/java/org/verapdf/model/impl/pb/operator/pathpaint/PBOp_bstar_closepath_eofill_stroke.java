package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.factory.operator.GraphicState;
import org.verapdf.model.operator.Op_bstar_closepath_eofill_stroke;
import org.verapdf.model.tools.resources.PDInheritableResources;

import java.util.List;

/**
 * Operator which closes, fills, and then strokes the path.
 * Uses the even-odd rule to determine the region to fill
 *
 * @author Timur Kamalov
 */
public class PBOp_bstar_closepath_eofill_stroke extends PBOpFillAndStroke
		implements Op_bstar_closepath_eofill_stroke {

	/** Type name for {@code PBOp_bstar_closepath_eofill_stroke} */
	public static final String OP_BSTAR_CLOSEPATH_EOFILL_STROKE_TYPE
			= "Op_bstar_closepath_eofill_stroke";

	/**
	 * Default constructor
	 *
	 * @param arguments arguments for current operator, must be empty.
	 * @param state graphic state for current operator
	 * @param resources resources for tilling pattern if it`s used
	 */
	public PBOp_bstar_closepath_eofill_stroke(List<COSBase> arguments,
											  final GraphicState state,
											  final PDInheritableResources resources) {
		super(arguments, state, resources, OP_BSTAR_CLOSEPATH_EOFILL_STROKE_TYPE);
	}
}
