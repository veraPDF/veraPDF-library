package org.verapdf.model.impl.pb.operator.markedcontent;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosName;
import org.verapdf.model.impl.pb.cos.PBCosName;
import org.verapdf.model.operator.Op_MP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Operator which designates a marked-content point
 *
 * @author Timur Kamalov
 */
public class PBOp_MP extends PBOpMarkedContent implements Op_MP {

	/** Type name for {@code PBOp_MP} */
    public static final String OP_MP_TYPE = "Op_MP";

    public PBOp_MP(List<COSBase> arguments) {
        super(arguments, OP_MP_TYPE);
    }

    @Override
    public List<? extends Object> getLinkedObjects(
            String link) {
        if (TAG.equals(link)) {
            return this.getTag();
        }
        return super.getLinkedObjects(link);
    }

	@Override
	protected List<CosName> getTag() {
		if (!this.arguments.isEmpty()) {
			COSBase name = this.arguments
					.get(this.arguments.size() - 1);
			if (name instanceof COSName) {
				List<CosName> list =
						new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				list.add(new PBCosName((COSName) name));
				return Collections.unmodifiableList(list);
			}
		}
		return Collections.emptyList();
	}

}
