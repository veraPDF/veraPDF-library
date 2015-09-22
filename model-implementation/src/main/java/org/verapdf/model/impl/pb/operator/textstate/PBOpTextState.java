package org.verapdf.model.impl.pb.operator.textstate;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpTextState;

import java.util.List;

/**
 * Base class for all text state operators
 *
 * @author Timur Kamalov
 */
public class PBOpTextState extends PBOperator implements OpTextState {

    protected PBOpTextState(List<COSBase> arguments, final String opType) {
        super(arguments, opType);
    }

}
