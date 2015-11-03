package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.PDResources;
import org.verapdf.model.factory.operator.GraphicState;
import org.verapdf.model.operator.Op_F_fill_obsolete;

import java.util.List;

/**
 * Operator equivalent to f
 *
 * @author Timur Kamalov
 */
public class PBOp_F_fill_obsolete extends PBOpFillPaint implements Op_F_fill_obsolete {

	/** Type name for {@code PBOp_F_fill_obsolete} */
	public static final String OP_F_FILL_OBSOLETE_TYPE = "Op_F_fill_obsolete";

	/**
	 * Default constructor
	 *
	 * @param arguments arguments for current operator, must be empty.
	 * @param state graphic state for current operator
	 * @param resources resources for tilling pattern if it`s used
	 */
	public PBOp_F_fill_obsolete(List<COSBase> arguments,
				final GraphicState state, final PDResources resources) {
		super(arguments, state, resources, OP_F_FILL_OBSOLETE_TYPE);
	}

}
