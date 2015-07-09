package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_FStar;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_FStar extends PBOpPathPaint implements Op_FStar {

    private static final String OP_FSTAR_TYPE = "Op_FStar";

    public PBOp_FStar(List<COSBase> arguments) {
        super(arguments);
        setType(OP_FSTAR_TYPE);
    }

}
