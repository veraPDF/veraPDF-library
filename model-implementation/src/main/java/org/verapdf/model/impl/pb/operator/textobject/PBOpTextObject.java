package org.verapdf.model.impl.pb.operator.textobject;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpTextObject;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOpTextObject extends PBOperator implements OpTextObject {

    public static final String OP_TEXT_OBJECT_TYPE = "OpTextObject";

    public PBOpTextObject(List<COSBase> arguments) {
        super(arguments, OP_TEXT_OBJECT_TYPE);
    }

}
