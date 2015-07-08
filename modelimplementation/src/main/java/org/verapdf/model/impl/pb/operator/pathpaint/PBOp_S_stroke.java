package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_S_stroke;
import org.verapdf.model.pdlayer.PDColorSpace;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_S_stroke extends PBOpPathPaint implements Op_S_stroke {

    public static final String OP_S_STROKE_TYPE = "Op_S_stroke";

    public static final String STROKE_CS = "strokeCS";

    public PBOp_S_stroke(List<COSBase> arguments) {
        super(arguments);
        setType(OP_S_STROKE_TYPE);
    }

    @Override
    public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(String link) {
        List<? extends org.verapdf.model.baselayer.Object> list;

        switch (link) {
            case STROKE_CS:
                list = this.getStrokeCS();
                break;
            default: list = super.getLinkedObjects(link);
        }

        return list;
    }

    private List<PDColorSpace> getStrokeCS() {
        List<PDColorSpace> list = new ArrayList<>();
        //TODO:
        return list;
    }

}
