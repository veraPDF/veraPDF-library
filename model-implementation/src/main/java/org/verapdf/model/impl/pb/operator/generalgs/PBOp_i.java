package org.verapdf.model.impl.pb.operator.generalgs;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.operator.Op_i;

import java.util.List;

/**
 * Operator defining the flatness tolerance in the graphics state
 *
 * @author Timur Kamalov
 */
public class PBOp_i extends PBOpGeneralGS implements Op_i {

	/** Type name for {@code PBOp_i} */
	public static final String OP_I_TYPE = "Op_i";

	/** Name of link to the flatness */
    public static final String FLATNESS = "flatness";

    public PBOp_i(List<COSBase> arguments) {
        super(arguments, OP_I_TYPE);
    }

    @Override
    public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(
            String link) {
        if (FLATNESS.equals(link)) {
            return this.getFlatness();
        }
        return super.getLinkedObjects(link);
    }

    private List<CosReal> getFlatness() {
        return this.getLastReal();
    }

}
