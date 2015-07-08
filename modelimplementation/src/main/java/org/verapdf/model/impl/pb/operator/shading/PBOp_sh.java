package org.verapdf.model.impl.pb.operator.shading;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_sh;
import org.verapdf.model.pdlayer.PDShading;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_sh extends PBOpShading implements Op_sh {

    public static final String OP_SH_TYPE = "Op_sh";

    public static final String SHADING = "shading";

    public PBOp_sh(List<COSBase> arguments) {
        super(arguments);
        setType(OP_SH_TYPE);
    }

    @Override
    public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(String link) {
        List<? extends org.verapdf.model.baselayer.Object> list;

        switch (link) {
            case SHADING:
                list = this.getShading();
                break;
            default: list = super.getLinkedObjects(link);
        }

        return list;
    }

    private List<PDShading> getShading() {
        List<PDShading> list = new ArrayList<>();
        //TODO:
        return list;
    }

}
