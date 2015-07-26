package org.verapdf.model.impl.pb.pd.colors;

import java.util.ArrayList;
import java.util.List;

import org.verapdf.model.baselayer.Object;
import org.verapdf.model.factory.colors.ColorSpaceFactory;
import org.verapdf.model.pdlayer.PDColorSpace;
import org.verapdf.model.pdlayer.PDIndexed;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDIndexed extends PBoxPDColorSpace implements PDIndexed {

	public static final String BASE = "base";
	public static final String INDEXED_TYPE = "PDIndexed";

	public PBoxPDIndexed(org.apache.pdfbox.pdmodel.graphics.color.PDIndexed simplePDObject) {
		super(simplePDObject);
		setType(INDEXED_TYPE);
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		List<? extends Object> list;

		switch (link) {
			case BASE:
				list = this.getBase();
				break;
			default:
				list = super.getLinkedObjects(link);
				break;
		}

		return list;
	}

	private List<PDColorSpace> getBase() {
		List<PDColorSpace> base = new ArrayList<>();
		org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace baseColorSpace =
				((org.apache.pdfbox.pdmodel.graphics.color.PDIndexed) simplePDObject).getBaseColorSpace();
		PDColorSpace colorSpace = ColorSpaceFactory.getColorSpace(baseColorSpace);
		if (colorSpace != null) {
			base.add(colorSpace);
		}
		return base;
	}
}
