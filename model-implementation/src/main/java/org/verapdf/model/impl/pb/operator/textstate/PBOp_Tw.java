package org.verapdf.model.impl.pb.operator.textstate;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.operator.Op_Tw;

import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_Tw extends PBOpTextState implements Op_Tw {

	public static final String OP_TW_TYPE = "Op_Tw";

	public static final String WORD_SPACE = "wordSpace";

	public PBOp_Tw(List<COSBase> arguments) {
		super(arguments, OP_TW_TYPE);
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		if (WORD_SPACE.equals(link)) {
			return this.getWordSpace();
		}
		return super.getLinkedObjects(link);
	}

	private List<CosReal> getWordSpace() {
		return this.getLastReal();
	}

}
