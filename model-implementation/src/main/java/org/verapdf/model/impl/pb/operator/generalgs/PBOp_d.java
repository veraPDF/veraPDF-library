package org.verapdf.model.impl.pb.operator.generalgs;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosArray;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.impl.pb.cos.PBCosArray;
import org.verapdf.model.operator.Op_d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Operator defining the line dash pattern in the graphics state
 *
 * @author Timur Kamalov
 */
public class PBOp_d extends PBOpGeneralGS implements Op_d {

	/** Type name for {@code PBOp_d} */
    public static final String OP_D_TYPE = "Op_d";

	/** Name of link to the dash array */
    public static final String DASH_ARRAY = "dashArray";
	/** Name of link to the dash phase */
    public static final String DASH_PHASE = "dashPhase";

    public PBOp_d(List<COSBase> arguments) {
        super(arguments, OP_D_TYPE);
    }

    @Override
    public List<? extends Object> getLinkedObjects(
            String link) {
        switch (link) {
        case DASH_ARRAY:
            return this.getDashArray();
        case DASH_PHASE:
            return this.getDashPhase();
        default:
            return super.getLinkedObjects(link);
        }
    }

    private List<CosArray> getDashArray() {
        if (this.arguments.size() > 1) {
			COSBase array = this.arguments
					.get(this.arguments.size() - 2);
			if (array instanceof COSArray) {
				List<CosArray> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				list.add(new PBCosArray((COSArray) array));
				return Collections.unmodifiableList(list);
			}
        }
        return Collections.emptyList();
    }

    private List<CosReal> getDashPhase() {
        return this.getLastReal();
    }

}
