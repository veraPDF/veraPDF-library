package org.verapdf.model.impl.pb.operator.markedcontent;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_EMC;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_EMC extends PBOpMarkedContent implements Op_EMC {

    public static final String OP_EMC_TYPE = "Op_EMC";

    public PBOp_EMC(List<COSBase> arguments) {
        super(arguments);
        setType(OP_EMC_TYPE);
    }

}
