package org.verapdf.model.impl.pb.operator.pathconstruction;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.impl.pb.cos.PBCosReal;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpPathConstruction;

import java.util.ArrayList;
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

    protected List<CosReal> getListOfReals() {
        List<CosReal> list = new ArrayList<>();
        if (!this.arguments.isEmpty()) {
            for (COSBase base : this.arguments) {
                if (base instanceof COSArray) {
                    for (COSBase arg : (COSArray) base) {
                        if (arg instanceof COSNumber) {
                            list.add(new PBCosReal((COSNumber) arg));
                        }
                    }
                } else if (base instanceof COSNumber) {
                    list.add(new PBCosReal((COSNumber) base));
                }
            }
        }
        return list;
    }

}
