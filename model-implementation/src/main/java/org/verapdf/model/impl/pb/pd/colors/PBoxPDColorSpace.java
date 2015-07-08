package org.verapdf.model.impl.pb.pd.colors;

import org.verapdf.model.impl.pb.pd.PBoxPDResources;
import org.verapdf.model.pdlayer.PDColorSpace;

/**
 * Color space object
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDColorSpace extends PBoxPDResources implements PDColorSpace {

	protected PBoxPDColorSpace(org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace simplePDObject) {
		super(simplePDObject);
	}

	/**
	 * @return number of colorants
	 */
	@Override
	public Long getnrComponents() {
		Integer numberOfComponents = ((org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace) simplePDObject)
				.getNumberOfComponents();
		return Long.valueOf(numberOfComponents);
	}
}
