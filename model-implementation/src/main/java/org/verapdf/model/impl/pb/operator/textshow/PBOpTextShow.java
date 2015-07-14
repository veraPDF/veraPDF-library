package org.verapdf.model.impl.pb.operator.textshow;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpTextShow;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOpTextShow extends PBOperator implements OpTextShow {

	public static final String OP_TEXT_SHOW_TYPE = "OpTextShow";
	public static final Integer MAX_NUMBER_OF_ELEMENTS = Integer.valueOf(1);

	public PBOpTextShow(List<COSBase> arguments) {
		super(arguments);
		setType(OP_TEXT_SHOW_TYPE);
	}

}
