package org.verapdf.model.impl.pb.operator.markedcontent;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_DP;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_DP extends PBOpMarkedContent implements Op_DP {

	public static final String OP_DP_TYPE = "Op_DP";

	public PBOp_DP(List<COSBase> arguments) {
		super(arguments);
		setType(OP_DP_TYPE);
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
