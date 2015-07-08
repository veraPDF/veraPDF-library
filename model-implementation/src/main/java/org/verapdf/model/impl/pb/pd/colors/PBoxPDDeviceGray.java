package org.verapdf.model.impl.pb.pd.colors;

import org.verapdf.model.pdlayer.PDDeviceGray;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDDeviceGray extends PBoxPDColorSpace implements PDDeviceGray {

	public PBoxPDDeviceGray(org.apache.pdfbox.pdmodel.graphics.color.PDDeviceGray simplePDObject) {
		super(simplePDObject);
		setType("PDDeviceGray");
	}
}
