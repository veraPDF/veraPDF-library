package org.verapdf.model.impl.pb.operator.opcompability;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpCompatibility;

import java.util.List;

/**
 * Base class for compatibility operators
 *
 * @author Timur Kamalov
 */
public abstract class PBOpCompatibility extends PBOperator implements OpCompatibility {

    public PBOpCompatibility(List<COSBase> arguments, final String opType) {
        super(arguments, opType);
    }

}
