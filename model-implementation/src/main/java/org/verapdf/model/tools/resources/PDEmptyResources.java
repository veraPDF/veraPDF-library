package org.verapdf.model.tools.resources;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;

/**
 * @author Evgeniy Muravitskiy
 */
class PDEmptyResources extends PDResources {

	@Override
	public PDColorSpace getColorSpace(COSName name) {
		return null;
	}
}
