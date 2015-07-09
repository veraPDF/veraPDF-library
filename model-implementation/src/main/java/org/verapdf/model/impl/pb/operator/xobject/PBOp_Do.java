package org.verapdf.model.impl.pb.operator.xobject;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_Do;
import org.verapdf.model.pdlayer.PDXObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_Do extends PBOpXObject implements Op_Do {

    public static final String OP_DO_TYPE = "Op_Do";

    public static final String X_OBJECT = "xObject";

    public PBOp_Do(List<COSBase> arguments) {
        super(arguments);
        setType(OP_DO_TYPE);
    }

    @Override
    public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(String link) {
        List<? extends org.verapdf.model.baselayer.Object> list;

        switch (link) {
            case X_OBJECT:
                list = this.getXObject();
                break;
            default: list = super.getLinkedObjects(link);
        }

        return list;
    }

    private List<PDXObject> getXObject() {
        List<PDXObject> list = new ArrayList<>();
        //TODO:
        return list;
    }

}
