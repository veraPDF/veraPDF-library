package org.verapdf.model.impl.pb.operator.textstate;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpTextState;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOpTextState extends PBOperator implements OpTextState {

	public static final String OP_TEXT_STATE_TYPE = "OpTextState";

	// TODO : implement all operators
	public PBOpTextState(List<COSBase> arguments) {
		super(arguments);
		setType(OP_TEXT_STATE_TYPE);
	}

}
