package org.verapdf.model.impl.pb.operator.color;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpColor;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOpColor extends PBOperator implements OpColor {

    public static final String OP_COLOR_TYPE = "OpColor";

    public PBOpColor(List<COSBase> arguments) {
        super(arguments, OP_COLOR_TYPE);
    }

}
