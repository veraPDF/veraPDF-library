package org.verapdf.model.impl.pb.pd.colors;

import org.verapdf.model.pdlayer.PDLab;

/**
 * Lab color space
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDLab extends PBoxPDColorSpace implements PDLab {

	public static final String LAB_COLOR_SPACE_TYPE = "PDLab";

	public PBoxPDLab(
            org.apache.pdfbox.pdmodel.graphics.color.PDLab simplePDObject) {
        super(simplePDObject, LAB_COLOR_SPACE_TYPE);
    }
}
