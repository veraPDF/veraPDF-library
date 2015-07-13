package org.verapdf.model.impl.pb.operator.textposition;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpTextPosition;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOpTextPosition extends PBOperator implements OpTextPosition {

	public static final String OP_TEXT_POSITION_TYPE = "OpTextPosition";

	public PBOpTextPosition(List<COSBase> arguments) {
		super(arguments);
		setType(OP_TEXT_POSITION_TYPE);
	}

}
