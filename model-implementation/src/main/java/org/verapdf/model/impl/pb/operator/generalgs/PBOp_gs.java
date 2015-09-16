package org.verapdf.model.impl.pb.operator.generalgs;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.pb.pd.PBoxPDExtGState;
import org.verapdf.model.operator.Op_gs;
import org.verapdf.model.pdlayer.PDExtGState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Operator defining the specified parameters in the graphics state.
 *
 * @author Timur Kamalov
 */
public class PBOp_gs extends PBOpGeneralGS implements Op_gs {

	/** Type name for {@code PBOp_gs} */
    public static final String OP_GS_TYPE = "Op_gs";

	/** Name of link to the extended graphic state */
    public static final String EXT_G_STATE = "extGState";

    private PDExtendedGraphicsState extGState;

    public PBOp_gs(List<COSBase> arguments,
				   PDExtendedGraphicsState extGState) {
        super(arguments, OP_GS_TYPE);
        this.extGState = extGState;
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        if (EXT_G_STATE.equals(link)) {
            return this.getExtGState();
        }
        return super.getLinkedObjects(link);
    }

    private List<PDExtGState> getExtGState() {
        if (this.extGState != null) {
			List<PDExtGState> extGStates = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			extGStates.add(new PBoxPDExtGState(this.extGState));
			return Collections.unmodifiableList(extGStates);
        }
        return Collections.emptyList();
    }
}
