package org.verapdf.model.impl.pb.operator.textobject;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpTextObject;

import java.util.List;

/**
 * Base class for pdf text operators (BT and ET)
 *
 * @author Timur Kamalov
 */
public class PBOpTextObject extends PBOperator implements OpTextObject {

	/** Type name for {@code PBOpTextObject} */
    public static final String OP_TEXT_OBJECT_TYPE = "OpTextObject";

    public PBOpTextObject(List<COSBase> arguments) {
        super(arguments, OP_TEXT_OBJECT_TYPE);
    }

}
