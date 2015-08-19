package org.verapdf.model.impl.pb.pd.colors;

import org.verapdf.model.pdlayer.PDCalRGB;

/**
 * CalRGB color space
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDCalRGB extends PBoxPDColorSpace implements PDCalRGB {

	public static final String CAL_RGB_TYPE = "PDCalRGB";

	public PBoxPDCalRGB(
			org.apache.pdfbox.pdmodel.graphics.color.PDCalRGB simplePDObject) {
		super(simplePDObject, CAL_RGB_TYPE);
	}
}
