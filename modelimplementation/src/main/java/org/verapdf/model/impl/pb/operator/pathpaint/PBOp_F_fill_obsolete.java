package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_F_fill_obsolete;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_F_fill_obsolete extends PBOpPathPaint implements Op_F_fill_obsolete {

    public static final String OP_F_FILL_OBSOLETE_TYPE = "Op_F_fill_obsolete";

    public PBOp_F_fill_obsolete(List<COSBase> arguments) {
        super(arguments);
        setType(OP_F_FILL_OBSOLETE_TYPE);
    }

}
