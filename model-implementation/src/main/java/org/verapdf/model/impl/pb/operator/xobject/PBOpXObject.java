package org.verapdf.model.impl.pb.operator.xobject;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpXObject;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public abstract class PBOpXObject extends PBOperator implements OpXObject {

    public PBOpXObject(List<COSBase> arguments) {
        super(arguments);
    }

    public PBOpXObject(List<COSBase> arguments, final String opType) {
        super(arguments, opType);
    }

}
