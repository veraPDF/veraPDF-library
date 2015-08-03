package org.verapdf.model.impl.pb.operator.generalgs;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpGeneralGS;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public abstract class PBOpGeneralGS extends PBOperator implements OpGeneralGS {

    public static final int OPERANDS_COUNT = 1;

    protected PBOpGeneralGS(List<COSBase> arguments) {
        super(arguments);
    }

    protected PBOpGeneralGS(List<COSBase> arguments, final String opType) {
        super(arguments, opType);
    }

}
