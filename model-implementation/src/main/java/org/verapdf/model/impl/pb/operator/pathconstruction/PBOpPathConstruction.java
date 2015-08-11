package org.verapdf.model.impl.pb.operator.pathconstruction;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpPathConstruction;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public abstract class PBOpPathConstruction extends PBOperator implements
        OpPathConstruction {

    public static final String CONTROL_POINTS = "controlPoints";

    public PBOpPathConstruction(List<COSBase> arguments) {
        super(arguments);
    }

    public PBOpPathConstruction(List<COSBase> arguments, final String opType) {
        super(arguments, opType);
    }

}
