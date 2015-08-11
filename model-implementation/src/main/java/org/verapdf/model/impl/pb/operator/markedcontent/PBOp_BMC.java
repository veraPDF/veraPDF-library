package org.verapdf.model.impl.pb.operator.markedcontent;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.verapdf.model.coslayer.CosName;
import org.verapdf.model.impl.pb.cos.PBCosName;
import org.verapdf.model.operator.Op_BMC;

import java.util.ArrayList;
import java.util.List;

/**
 * Operator, which begin a marked-content sequence
 *
 * @author Timur Kamalov
 */
public class PBOp_BMC extends PBOpMarkedContent implements Op_BMC {

    public static final String OP_BMC_TYPE = "Op_BMC";

    public PBOp_BMC(List<COSBase> arguments) {
        super(arguments, OP_BMC_TYPE);
    }

    @Override
    public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(String link) {
        switch (link) {
            case TAG:
                return this.getTag();
            case PROPERTIES:
                return this.getPropertiesDict();
            default:
                return super.getLinkedObjects(link);
        }
    }

	protected List<CosName> getTag() {
		List<CosName> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
		if (!this.arguments.isEmpty()) {
			COSBase name = this.arguments
					.get(this.arguments.size() - 1);
			if (name instanceof COSName) {
				list.add(new PBCosName((COSName) name));
			}
		}
		return list;
	}

}
