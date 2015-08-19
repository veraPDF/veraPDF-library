package org.verapdf.model.impl.pb.pd.pattern;

import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import org.verapdf.model.impl.pb.pd.colors.PBoxPDColorSpace;
import org.verapdf.model.pdlayer.PDPattern;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDPattern extends PBoxPDColorSpace implements PDPattern {

	protected PBoxPDPattern(PDAbstractPattern simplePDObject,
							final String type) {
		super(simplePDObject, type);
	}

}
