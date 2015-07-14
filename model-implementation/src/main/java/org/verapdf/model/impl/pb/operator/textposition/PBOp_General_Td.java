package org.verapdf.model.impl.pb.operator.textposition;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.impl.pb.cos.PBCosReal;
import org.verapdf.model.impl.pb.operator.textstate.PBOpTextState;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public abstract class PBOp_General_Td extends PBOpTextPosition {

	public static final String HORIZONTAL_OFFSET = "horizontalOffset";
	public static final String VERTICAL_OFFSET = "verticalOffset";

	protected PBOp_General_Td(List<COSBase> arguments) {
		super(arguments);
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		List<? extends Object> list;

		switch (link) {
			case VERTICAL_OFFSET:
				list = getVerticalOffset();
				break;
			case HORIZONTAL_OFFSET:
				list = getHorizontalOffset();
				break;
			default:
				list = super.getLinkedObjects(link);
				break;
		}

		return list;
	}

	private List<CosReal> getHorizontalOffset() {
		List<CosReal> offset = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
		final int size = this.arguments.size();
		COSBase base = size > 1 ? this.arguments.get(size - 2) : null;
		if (base instanceof COSNumber) {
			offset.add(new PBCosReal((COSNumber) base));
		}
		return offset;
	}

	private List<CosReal> getVerticalOffset() {
		return this.getLastReal();
	}
}
