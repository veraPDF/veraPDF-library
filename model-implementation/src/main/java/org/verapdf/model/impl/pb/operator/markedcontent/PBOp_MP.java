package org.verapdf.model.impl.pb.operator.markedcontent;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.verapdf.model.coslayer.CosName;
import org.verapdf.model.impl.pb.cos.PBCosName;
import org.verapdf.model.operator.Op_MP;

import java.util.ArrayList;
import java.util.List;

/**
 * Operator, which designate a marked-content point
 *
 * @author Timur Kamalov
 */
public class PBOp_MP extends PBOpMarkedContent implements Op_MP {

    public static final String OP_MP_TYPE = "Op_MP";

    public PBOp_MP(List<COSBase> arguments) {
        super(arguments, OP_MP_TYPE);
    }

    @Override
    public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(
            String link) {
        if (TAG.equals(link)) {
            return this.getTag();
        }
        return super.getLinkedObjects(link);
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
