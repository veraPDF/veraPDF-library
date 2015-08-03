package org.verapdf.model.impl.pb.operator.opclip;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_WStar;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_WStar extends PBOpClip implements Op_WStar {

    public static final String OP_WSTAR_TYPE = "Op_WStar";

    public PBOp_WStar(List<COSBase> arguments) {
        super(arguments, OP_WSTAR_TYPE);
    }

}
