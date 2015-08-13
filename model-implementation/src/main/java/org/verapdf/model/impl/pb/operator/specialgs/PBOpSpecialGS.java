package org.verapdf.model.impl.pb.operator.specialgs;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpSpecialGS;

import java.util.List;

/**
 * Base class for special graphic state operators
 *
 * @author Timur Kamalov
 */
public abstract class PBOpSpecialGS extends PBOperator implements OpSpecialGS {

    public PBOpSpecialGS(List<COSBase> arguments, String opType) {
        super(arguments, opType);
    }

}

