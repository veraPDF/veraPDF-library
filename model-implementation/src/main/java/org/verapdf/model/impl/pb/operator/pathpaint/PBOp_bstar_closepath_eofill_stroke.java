package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_bstar_closepath_eofill_stroke;
import org.verapdf.model.pdlayer.PDColorSpace;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_bstar_closepath_eofill_stroke extends PBOpPathPaint implements Op_bstar_closepath_eofill_stroke {

    public static final String OP_BSTAR_CLOSEPATH_EOFILL_STROKE_TYPE = "Op_bstar_closepath_eofill_stroke";

	public PBOp_bstar_closepath_eofill_stroke(List<COSBase> arguments,
											  org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace strokeColorSpace,
											  org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace fillColorSpace) {
		super(arguments, strokeColorSpace, fillColorSpace);
		setType(OP_BSTAR_CLOSEPATH_EOFILL_STROKE_TYPE);
	}

    @Override
    public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(String link) {
        List<? extends org.verapdf.model.baselayer.Object> list;

        switch (link) {
            case FILL_CS:
                list = this.getFillCS();
                break;
            case STROKE_CS:
                list = this.getStrokeCS();
                break;
            default: list = super.getLinkedObjects(link);
        }

        return list;
    }

    private List<PDColorSpace> getFillCS() {
        List<PDColorSpace> list = new ArrayList<>();
        //TODO:
        return list;
    }

    private List<PDColorSpace> getStrokeCS() {
        List<PDColorSpace> list = new ArrayList<>();
        //TODO:
        return list;
    }

}
