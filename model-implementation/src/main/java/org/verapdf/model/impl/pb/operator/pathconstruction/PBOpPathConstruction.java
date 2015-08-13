package org.verapdf.model.impl.pb.operator.pathconstruction;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpPathConstruction;

import java.util.List;

/**
 * Base class for path construction operators
 *
 * @author Timur Kamalov
 */
public abstract class PBOpPathConstruction extends PBOperator implements
        OpPathConstruction {

	/** Name of link to the control ponts */
    public static final String CONTROL_POINTS = "controlPoints";

    public PBOpPathConstruction(List<COSBase> arguments, final String opType) {
        super(arguments, opType);
    }

}
