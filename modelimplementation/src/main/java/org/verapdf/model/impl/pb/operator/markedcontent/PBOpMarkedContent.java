package org.verapdf.model.impl.pb.operator.markedcontent;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpMarkedContent;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public abstract class PBOpMarkedContent extends PBOperator implements OpMarkedContent {

    public PBOpMarkedContent(List<COSBase> arguments) {
        super(arguments);
    }

}
