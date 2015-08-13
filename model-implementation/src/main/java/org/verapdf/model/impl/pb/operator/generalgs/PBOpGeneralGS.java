package org.verapdf.model.impl.pb.operator.generalgs;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpGeneralGS;

import java.util.List;

/**
 * Base class of general graphic state operators
 *
 * @author Timur Kamalov
 */
public abstract class PBOpGeneralGS extends PBOperator implements OpGeneralGS {

    protected PBOpGeneralGS(List<COSBase> arguments, final String opType) {
        super(arguments, opType);
    }

}
