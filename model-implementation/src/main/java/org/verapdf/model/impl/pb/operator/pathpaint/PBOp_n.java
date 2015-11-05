package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_n;

import java.util.List;

/**
 * Operator which ends the path object without filling or
 * stroking it. This operator is a “path-painting no-op,”
 * used primarily for the side effect of changing the current
 * clipping path
 *
 * @author Timur Kamalov
 */
public class PBOp_n extends PBOpPathPaint implements Op_n {

	/** Type name for {@code PBOp_n} */
	public static final String OP_N_TYPE = "Op_n";

	/**
	 * Default constructor
	 *
	 * @param arguments arguments for current operator, must be empty.
	 */
	public PBOp_n(List<COSBase> arguments) {
		super(arguments, null, null, null, null, OP_N_TYPE);
	}

}
