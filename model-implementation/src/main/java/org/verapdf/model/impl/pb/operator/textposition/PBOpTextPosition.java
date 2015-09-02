package org.verapdf.model.impl.pb.operator.textposition;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpTextPosition;

import java.util.List;

/**
 * Base class for all text position operators
 *
 * @author Timur Kamalov
 */
public class PBOpTextPosition extends PBOperator implements OpTextPosition {

	/**
	 * Type name for {@code PBOpTextPosition}. Current type apply to
	 * Tm and T* operators
	 */
    public static final String OP_TEXT_POSITION_TYPE = "OpTextPosition";

    public PBOpTextPosition(List<COSBase> arguments) {
        this(arguments, OP_TEXT_POSITION_TYPE);
    }

    public PBOpTextPosition(List<COSBase> arguments, final String opType) {
        super(arguments, opType);
    }

}
