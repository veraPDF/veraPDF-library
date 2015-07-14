package org.verapdf.model.impl.pb.operator.textshow;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpTextShow;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public abstract class PBOpTextShow extends PBOperator implements OpTextShow {

	protected PBOpTextShow(List<COSBase> arguments) {
		super(arguments);
	}

}
