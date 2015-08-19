package org.verapdf.model.impl.pb.pd.colors;

import org.verapdf.model.pdlayer.PDCalGray;

/**
 * CalGray color space
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDCalGray extends PBoxPDColorSpace implements PDCalGray {

	public static final String CAL_GRAY_TYPE = "PDCalGray";

	public PBoxPDCalGray(
			org.apache.pdfbox.pdmodel.graphics.color.PDCalGray simplePDObject) {
		super(simplePDObject, CAL_GRAY_TYPE);
	}
}
