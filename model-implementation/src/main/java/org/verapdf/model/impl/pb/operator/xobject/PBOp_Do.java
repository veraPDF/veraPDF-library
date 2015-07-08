package org.verapdf.model.impl.pb.operator.xobject;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_Do;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_Do extends PBOpXObject implements Op_Do {

    public static final String OP_DO_TYPE = "Op_Do";

    public PBOp_Do(List<COSBase> arguments) {
        super(arguments);
        setType(OP_DO_TYPE);
    }

}
