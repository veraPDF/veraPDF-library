package org.verapdf.model.impl.pb.operator.color;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpColor;

import java.util.List;

/**
 * Base class for pdf color operators like CS, cs, SC, SCN,
 * sc, scn, G, g, RG, rg, K, k
 *
 * @author Timur Kamalov
 */
public class PBOpColor extends PBOperator implements OpColor {

	/** Type name for {@code PBOpColor} */
    public static final String OP_COLOR_TYPE = "OpColor";

    public PBOpColor(List<COSBase> arguments) {
        super(arguments, OP_COLOR_TYPE);
    }

}
