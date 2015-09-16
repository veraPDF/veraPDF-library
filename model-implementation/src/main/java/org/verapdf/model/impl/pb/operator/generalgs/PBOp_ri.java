package org.verapdf.model.impl.pb.operator.generalgs;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosRenderingIntent;
import org.verapdf.model.impl.pb.cos.PBCosRenderingIntent;
import org.verapdf.model.operator.Op_ri;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Operator defining the color rendering intent in the graphics state
 *
 * @author Timur Kamalov
 */
public class PBOp_ri extends PBOpGeneralGS implements Op_ri {

	/** Type name for {@code PBOp_ri} */
    public static final String OP_RI_TYPE = "Op_ri";

	/** Name of link to the rendering intent */
    public static final String RENDERING_INTENT = "renderingIntent";

    public PBOp_ri(List<COSBase> arguments) {
        super(arguments, OP_RI_TYPE);
    }

    @Override
    public List<? extends Object> getLinkedObjects(
            String link) {
        if (RENDERING_INTENT.equals(link)) {
            return this.getRenderingIntent();
        }
        return super.getLinkedObjects(link);
    }

    private List<CosRenderingIntent> getRenderingIntent() {
		if (!this.arguments.isEmpty()) {
			COSBase base = this.arguments
					.get(this.arguments.size() - 1);
			if (base instanceof COSName) {
				List<CosRenderingIntent> list =
						new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				list.add(new PBCosRenderingIntent((COSName) base));
				return Collections.unmodifiableList(list);
			}
		}
        return Collections.emptyList();
    }

}
