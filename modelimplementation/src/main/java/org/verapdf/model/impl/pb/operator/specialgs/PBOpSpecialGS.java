package org.verapdf.model.impl.pb.operator.specialgs;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpSpecialGS;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public abstract class PBOpSpecialGS extends PBOperator implements OpSpecialGS {

    public PBOpSpecialGS(List<COSBase> arguments) {
        super(arguments);
    }

}

