package org.verapdf.model.impl.pb.pd.colors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.verapdf.model.baselayer.Object;
import org.verapdf.model.factory.colors.ColorSpaceFactory;
import org.verapdf.model.pdlayer.PDColorSpace;
import org.verapdf.model.pdlayer.PDSeparation;

/**
 * Separation color space
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDSeparation extends PBoxPDColorSpace implements PDSeparation {

	public static final String SEPARATION_TYPE = "PDSeparation";

    public static final String ALTERNATE = "alternate";

    public PBoxPDSeparation(
            org.apache.pdfbox.pdmodel.graphics.color.PDSeparation simplePDObject) {
        super(simplePDObject, SEPARATION_TYPE);
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        if (ALTERNATE.equals(link)) {
            return this.getAlternate();
        }
        return super.getLinkedObjects(link);
    }

    /**
     * @return a {@link List} of alternate {@link PDColorSpace} objects
     */
    public List<PDColorSpace> getAlternate() {
        org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace space =
				((org.apache.pdfbox.pdmodel.graphics.color.PDSeparation) this.simplePDObject)
                .getAlternateColorSpace();
        PDColorSpace currentSpace = ColorSpaceFactory.getColorSpace(space);
        if (currentSpace != null) {
			List<PDColorSpace> colorSpace = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			colorSpace.add(currentSpace);
			return Collections.unmodifiableList(colorSpace);
        }
        return Collections.emptyList();
    }
}
