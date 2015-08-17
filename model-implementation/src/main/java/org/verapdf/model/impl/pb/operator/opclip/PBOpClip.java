package org.verapdf.model.impl.pb.operator.opclip;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpClip;

import java.util.List;

/**
 * Base class of clipping path operators
 *
 * @author Timur Kamalov
 */
public abstract class PBOpClip extends PBOperator implements OpClip {

    protected PBOpClip(List<COSBase> arguments, final String opType) {
        super(arguments, opType);
    }

}
