package org.verapdf.model.impl.pb.operator.inlineimage;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpInlineImage;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOpInlineImage extends PBOperator implements OpInlineImage {

	public static final String OP_INLINE_IMAGE = "OpInlineImage";

	public PBOpInlineImage(List<COSBase> arguments) {
		super(arguments);
		setType(OP_INLINE_IMAGE);
	}

}
