package org.verapdf.model.impl.pb.operator.generalgs;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.pb.pd.PBoxPDExtGState;
import org.verapdf.model.operator.Op_gs;
import org.verapdf.model.pdlayer.PDExtGState;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBOp_gs extends PBOpGeneralGS implements Op_gs {

    public static final String OP_GS_TYPE = "Op_gs";
	public static final String EXT_G_STATE = "extGState";
	public static final Integer MAX_NUMBER_OF_STATES = Integer.valueOf(1);

	private PDExtendedGraphicsState extGState;

	public PBOp_gs(List<COSBase> arguments, PDExtendedGraphicsState extGState) {
        super(arguments);
		this.extGState = extGState;
        setType(OP_GS_TYPE);
    }

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		List<? extends Object> list;

		switch (link) {
			case EXT_G_STATE:
				list = getExtGState();
				break;
			default:
				list = super.getLinkedObjects(link);
				break;
		}

		return list;
	}

	private List<PDExtGState> getExtGState() {
		List<PDExtGState> extGStates = new ArrayList<>(MAX_NUMBER_OF_STATES);
		if (extGState != null) {
			extGStates.add(new PBoxPDExtGState(extGState));
		}
		return extGStates;
	}
}
