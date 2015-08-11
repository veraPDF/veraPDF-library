package org.verapdf.model.impl.pb.operator.generalgs;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.verapdf.model.coslayer.CosRenderingIntent;
import org.verapdf.model.impl.pb.cos.PBCosRenderingIntent;
import org.verapdf.model.operator.Op_ri;

import java.util.ArrayList;
import java.util.List;

/**
 * Operator, which set the color rendering intent in the graphics state
 *
 * @author Timur Kamalov
 */
public class PBOp_ri extends PBOpGeneralGS implements Op_ri {

    public static final String OP_RI_TYPE = "Op_ri";
    public static final String RENDERING_INTENT = "renderingIntent";

    public PBOp_ri(List<COSBase> arguments) {
        super(arguments, OP_RI_TYPE);
    }

    @Override
    public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(
            String link) {
        if (RENDERING_INTENT.equals(link)) {
            return this.getRenderingIntent();
        }
        return super.getLinkedObjects(link);
    }

    private List<CosRenderingIntent> getRenderingIntent() {
        List<CosRenderingIntent> list = new ArrayList<>(OPERANDS_COUNT);
		if (!this.arguments.isEmpty()) {
			COSBase base = this.arguments.get(this.arguments.size() - 1);
			if (base instanceof COSName) {
				list.add(new PBCosRenderingIntent((COSName) base));
			}
		}
        return list;
    }

}
