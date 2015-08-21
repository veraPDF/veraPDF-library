package org.verapdf.model.impl.pb.pd.images;

import org.verapdf.model.coslayer.CosDict;
import org.verapdf.model.pdlayer.PDInlineImage;
import org.verapdf.model.pdlayer.PDXImage;
import org.verapdf.model.pdlayer.PDXObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDInlineImage extends PBoxPDXImage implements PDInlineImage {

	public static final String INLINE_IMAGE_TYPE = "PDInlineImage";

	public PBoxPDInlineImage(org.apache.pdfbox.pdmodel.graphics.image.PDInlineImage simplePDObject) {
		super(simplePDObject, INLINE_IMAGE_TYPE);
	}

	@Override
	public String getF() {
		List<String> filters = ((org.apache.pdfbox.pdmodel.graphics.image.PDInlineImage) simplePDObject)
				.getFilters();
		if (filters != null) {
			StringBuilder builder = new StringBuilder();
			for (String filter : filters) {
				builder.append(filter).append(' ');
			}
			// need to discard last white space
			return builder.substring(0, builder.length() - 1);
		}
		return null;
	}

	@Override
	public String getSubtype() {
		return null;
	}

	@Override
	protected List<PDXImage> getAlternates() {
		return new ArrayList<>(0);
	}

	@Override
	protected List<PDXObject> getSMask() {
		return new ArrayList<>(0);
	}

	@Override
	protected List<CosDict> getOPI() {
		return new ArrayList<>(0);
	}
}
