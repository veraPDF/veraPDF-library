package org.verapdf.model.impl.pb.operator.shading;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpShading;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public abstract class PBOpShading extends PBOperator implements OpShading {

    public PBOpShading(List<COSBase> arguments) {
        super(arguments);
    }

    public PBOpShading(List<COSBase> arguments, String opType) {
        super(arguments, opType);
    }

}
