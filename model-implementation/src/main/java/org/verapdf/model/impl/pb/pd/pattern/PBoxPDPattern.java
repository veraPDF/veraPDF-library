package org.verapdf.model.impl.pb.pd.pattern;

import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import org.verapdf.model.impl.pb.pd.PBoxPDResources;
import org.verapdf.model.pdlayer.PDPattern;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDPattern extends PBoxPDResources implements PDPattern {

	protected PBoxPDPattern(PDAbstractPattern simplePDObject) {
		super(simplePDObject);
	}
}
