package org.verapdf.model.impl.pb.operator.generalgs;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_w_line_width;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_w_line_width extends PBOpGeneralGS implements Op_w_line_width {

    public static final String OP_W_LINE_WIDTH_TYPE = "Op_w_line_width";

    public PBOp_w_line_width(List<COSBase> arguments) {
        super(arguments);
        setType(OP_W_LINE_WIDTH_TYPE);
    }

}
