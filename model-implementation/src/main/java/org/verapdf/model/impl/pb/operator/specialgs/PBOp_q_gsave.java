package org.verapdf.model.impl.pb.operator.specialgs;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.impl.pb.operator.generalgs.PBOpGeneralGS;
import org.verapdf.model.operator.Op_q_gsave;

import java.util.List;

/**
 * Operator defining the current graphics state on the
 * graphics state stack
 *
 * @author Timur Kamalov
 */
public class PBOp_q_gsave extends PBOpGeneralGS implements Op_q_gsave {

	/** Type name for {@code PBOp_q_gsave} */
    public static final String OP_Q_GSAVE_TYPE = "Op_q_gsave";

    private final int nestingLevel;

    public PBOp_q_gsave(List<COSBase> arguments, int  nestingLevel) {
        super(arguments, OP_Q_GSAVE_TYPE);
        this.nestingLevel = nestingLevel;
    }

	/**
	 * @return depth of graphics state
	 */
    @Override
    public Long getnestingLevel() {
        return Long.valueOf(this.nestingLevel);
    }

}
