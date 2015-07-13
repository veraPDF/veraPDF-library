package org.verapdf.model.impl.pb.operator.markedcontent;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_BDC;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_BDC extends PBOpMarkedContent implements Op_BDC {

	public static final String OP_BDC_TYPE = "Op_BDC";

	public PBOp_BDC(List<COSBase> arguments) {
		super(arguments);
		setType(OP_BDC_TYPE);
	}

	@Override
	public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(String link) {
		List<? extends org.verapdf.model.baselayer.Object> list;

		switch (link) {
			case TAG:
				list = this.getTag();
				break;
			case PROPERTIES:
				list = this.getPropertiesDict();
				break;
			default: list = super.getLinkedObjects(link);
		}

		return list;
	}

}
