package org.verapdf.model.impl.pb.pd.colors;

import org.verapdf.model.pdlayer.PDDeviceRGB;

/**
 * DeviceRGB color space
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDDeviceRGB extends PBoxPDColorSpace implements PDDeviceRGB {

	public PBoxPDDeviceRGB(org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB simplePDObject) {
		super(simplePDObject);
		setType("PDDeviceRGB");
	}
}
