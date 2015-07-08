package org.verapdf.model.impl.pb.pd.colors;

import org.verapdf.model.pdlayer.PDDeviceCMYK;

/**
 * DeviceCMYK color space
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDDeviceCMYK extends PBoxPDColorSpace implements PDDeviceCMYK {

	public PBoxPDDeviceCMYK(org.apache.pdfbox.pdmodel.graphics.color.PDDeviceCMYK simplePDObject) {
		super(simplePDObject);
		setType("PDDeviceCMYK");
	}
}
