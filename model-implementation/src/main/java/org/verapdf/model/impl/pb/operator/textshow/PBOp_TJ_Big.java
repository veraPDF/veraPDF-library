package org.verapdf.model.impl.pb.operator.textshow;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosArray;
import org.verapdf.model.impl.pb.cos.PBCosArray;
import org.verapdf.model.operator.Op_TJ_Big;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_TJ_Big extends PBOpTextShow implements Op_TJ_Big{

	public static final String SPECIAL_STRINGS = "specialStrings";
	public static final String OP_TJ_BIG_TYPE = "Op_TJ_Big";

	public PBOp_TJ_Big(List<COSBase> arguments, PDFont font) {
		super(arguments, font);
		setType(OP_TJ_BIG_TYPE);
	}

	@Override
	public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(String link) {
		List<? extends Object> list;

		switch (link) {
			case SPECIAL_STRINGS:
				list = this.getSpecialStrings();
				break;
			default:
				list = super.getLinkedObjects(link);
				break;
		}

		return list;
	}

	private List<CosArray> getSpecialStrings() {
		List<CosArray> array = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
		COSBase base = !this.arguments.isEmpty() ? this.arguments.get(this.arguments.size() - 1) : null;
		if (base instanceof COSArray) {
			array.add(new PBCosArray((COSArray) base));
		}
		return array;
	}
}
