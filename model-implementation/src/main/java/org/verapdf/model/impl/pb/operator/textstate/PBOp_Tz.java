package org.verapdf.model.impl.pb.operator.textstate;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.operator.Op_Tz;

import java.util.List;

/**
 * Operator defining the horizontal scaling
 *
 * @author Evgeniy Muravitskiy
 */
public class PBOp_Tz extends PBOpTextState implements Op_Tz {

	/** Type name for {@code PBOp_Tz} */
    public static final String OP_TZ_TYPE = "Op_Tz";

	/** Name of link to the scale */
    public static final String SCALE = "scale";

    public PBOp_Tz(List<COSBase> arguments) {
        super(arguments, OP_TZ_TYPE);
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        if (SCALE.equals(link)) {
            return this.getScale();
        }
        return super.getLinkedObjects(link);
    }

    private List<CosReal> getScale() {
        return this.getLastReal();
    }
}
