package org.verapdf.model.impl.pb.operator.opcompability;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpCompatibility;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public abstract class PBOpCompability extends PBOperator implements OpCompatibility {

    public PBOpCompability(List<COSBase> arguments) {
        super(arguments);
    }

}
