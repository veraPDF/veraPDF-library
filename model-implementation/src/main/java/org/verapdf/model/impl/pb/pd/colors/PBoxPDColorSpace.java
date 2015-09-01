package org.verapdf.model.impl.pb.pd.colors;

import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import org.verapdf.model.impl.pb.pd.PBoxPDResources;
import org.verapdf.model.pdlayer.PDColorSpace;

/**
 * Color space object
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDColorSpace extends PBoxPDResources implements PDColorSpace {

    protected PBoxPDColorSpace(
            org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace simplePDObject,
			final String type) {
        super(simplePDObject, type);
    }

	protected PBoxPDColorSpace(PDAbstractPattern simplePDObject,
							   final String type) {
		super(simplePDObject, type);
	}

    /**
     * @return number of colorants
     */
    @Override
    public Long getnrComponents() {
        if (this.simplePDObject instanceof org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace) {
            int numberOfComponents = ((org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace) this.simplePDObject)
                    .getNumberOfComponents();
            return Long.valueOf(numberOfComponents);
        } else if (this.simplePDObject instanceof PDAbstractPattern) {
            return Long.valueOf(-1);
        } else
            return null;
    }

}
