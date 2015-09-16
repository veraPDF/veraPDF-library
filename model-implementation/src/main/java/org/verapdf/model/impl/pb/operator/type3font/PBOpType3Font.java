package org.verapdf.model.impl.pb.operator.type3font;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpType3Font;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOpType3Font extends PBOperator implements OpType3Font {

	protected PBOpType3Font(List<COSBase> arguments, String type) {
		super(arguments, type);
	}

}
