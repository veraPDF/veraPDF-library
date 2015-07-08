package org.verapdf.model.impl.pb.operator.generalgs;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.verapdf.model.coslayer.CosRenderingIntent;
import org.verapdf.model.impl.pb.cos.PBCosRenderingIntent;
import org.verapdf.model.operator.Op_ri;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_ri extends PBOpGeneralGS implements Op_ri {

    public static final String OP_RI_TYPE = "Op_ri";

    public static final String RENDERING_INTENT = "renderingIntent";

    public PBOp_ri(List<COSBase> arguments) {
        super(arguments);
        setType(OP_RI_TYPE);
    }

    @Override
    public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(String link) {
        List<? extends org.verapdf.model.baselayer.Object> list;

        switch (link) {
            case RENDERING_INTENT:
                list = this.getRenderingIntent();
                break;
            default: list = super.getLinkedObjects(link);
        }

        return list;
    }

    private List<CosRenderingIntent> getRenderingIntent() {
        List<CosRenderingIntent> list = new ArrayList<>();
        if (!this.arguments.isEmpty() && this.arguments.get(0) instanceof COSName) {
            list.add(new PBCosRenderingIntent((COSName) this.arguments.get(0)));
        }
        return list;
    }

}
