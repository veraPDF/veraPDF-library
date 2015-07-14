package org.verapdf.model.impl.pb.operator.textshow;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSString;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosString;
import org.verapdf.model.impl.pb.cos.PBCosString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public abstract class PBOpStringTextShow extends PBOpTextShow {

	public static final String TEXT_STRING = "textString";

	protected PBOpStringTextShow(List<COSBase> arguments) {
		super(arguments);
	}

	@Override
	public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(String link) {
		List<? extends Object> list;

		switch (link) {
			case TEXT_STRING:
				list = this.getTextString();
				break;
			default:
				list = super.getLinkedObjects(link);
				break;
		}

		return list;
	}

	private List<CosString> getTextString() {
		List<CosString> string = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
		COSBase base = !this.arguments.isEmpty() ? this.arguments.get(this.arguments.size() - 1) : null;
		if (base instanceof COSString) {
			string.add(new PBCosString((COSString) base));
		}
		return string;
	}
}
