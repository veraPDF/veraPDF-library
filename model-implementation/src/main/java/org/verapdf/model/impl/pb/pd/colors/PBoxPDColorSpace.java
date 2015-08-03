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
            org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace simplePDObject) {
        super(simplePDObject);
    }

    protected PBoxPDColorSpace(PDAbstractPattern simplePDObject) {
        super(simplePDObject);
    }

    /**
     * @return number of colorants
     */
    @Override
    public Long getnrComponents() {
        if (simplePDObject instanceof org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace) {
            int numberOfComponents = ((org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace) simplePDObject)
                    .getNumberOfComponents();
            return Long.valueOf(numberOfComponents);
        } else if (simplePDObject instanceof PDAbstractPattern) {
            return Long.valueOf(-1);
        } else
            return null;
    }

}
