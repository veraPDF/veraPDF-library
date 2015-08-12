package org.verapdf.model.impl.pb.operator.shading;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.impl.pb.pd.pattern.PBoxPDShading;
import org.verapdf.model.operator.Op_sh;
import org.verapdf.model.pdlayer.PDShading;

import java.util.ArrayList;
import java.util.List;

/**
 * Operator, which paint the shape and color shading described
 * by a shading dictionary, subject to the current clipping path
 *
 * @author Timur Kamalov
 */
public class PBOp_sh extends PBOperator implements Op_sh {

	/** Type name for {@code PBOp_sh} */
    public static final String OP_SH_TYPE = "Op_sh";

	/** Shading link name for {@code Op_sh} operator */
    public static final String SHADING = "shading";

    private org.apache.pdfbox.pdmodel.graphics.shading.PDShading shading;

    public PBOp_sh(List<COSBase> arguments,
            org.apache.pdfbox.pdmodel.graphics.shading.PDShading shading) {
        super(arguments, OP_SH_TYPE);
        this.shading = shading;
    }

    @Override
    public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(
            String link) {
        if (SHADING.equals(link)) {
            return this.getShading();
        }
        return super.getLinkedObjects(link);
    }

    private List<PDShading> getShading() {
        List<PDShading> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
        if (this.shading != null) {
            list.add(new PBoxPDShading(this.shading));
        }
        return list;
    }

}
